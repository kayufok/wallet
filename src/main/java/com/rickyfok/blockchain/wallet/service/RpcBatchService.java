package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.model.EthBatchWorker;
import com.rickyfok.blockchain.wallet.repository.LogEthRepository;
import com.rickyfok.blockchain.wallet.util.Convertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class RpcBatchService {

    @Autowired
    LogEthRepository logEthRepository;

    @Autowired
    RpcApiService rpcApiService;

    @Autowired
    AddressService addressService;


    public Optional<Long> findMaxBatchId() {
        return Optional.ofNullable(logEthRepository.findMaxId());
    }

    public List<LogEth> retrieveEthBatchBySize(int size) {
        return IntStream
                .rangeClosed(1, size)
                .parallel()
                .mapToObj(Integer::toString)
                .map(i -> new LogEth().setStatusId(1L))
                .map(logEthRepository::save)
                .toList();
    }

    public void rpcAddressBatchEthStream() {

        var ethBatch = retrieveEthBatchBySize(20);

        var addressList = ethBatch.stream()
                .parallel()
                .peek(b -> System.out.println(Thread.currentThread().getName() + "batch id:" + b.getId()))
                .map(b -> new EthBatchWorker(b,rpcApiService.getApiResponse("eth", Convertor.blockFrom.apply(b.getId()), Convertor.blockTo.apply(b.getId()))))
                .peek(w -> logEthRepository.save(w.getLogEth().setStatusId(2L).setMessage(w.getAddressList().size() + " Address")))
                .flatMap(w -> Stream.of(w.getAddressList()))
                .flatMap(List::stream)
                .map(s -> new Address().setAddress(s))
                .toList();

        addressList.stream()
                .parallel()
                .filter(a -> addressService.getAddressCount(a.getAddress()) == 0)
                .forEach(a -> addressService.saveAddress(a));

    }


    public void rpcAddressBatchEth() {
        // retrieve next eth batch list by retrieveEthBatchBySize()
        var ethBatch = retrieveEthBatchBySize(50);
        var addressList = new ArrayList<String>();

        // Create an ExecutorService with a fixed thread pool size
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Future<List<String>>> futures = new ArrayList<>();

        for (LogEth logEth : ethBatch) {
            Callable<List<String>> callable = () -> {
                // Call rpcApiService.getApiResponse()
                var rpcAddressList = rpcApiService
                        .getApiResponse("eth", Convertor.blockFrom.apply(logEth.getId()), Convertor.blockTo.apply(logEth.getId()));

                // If response is null, update the status and return an empty list
                if (rpcAddressList == null || rpcAddressList.isEmpty()) {
                    logEth.setStatusId(2L)
                            .setMessage("0 Address");
                    return new ArrayList<>();
                }else {
                    logEth.setStatusId(2L)
                            .setMessage(rpcAddressList.size() + " Address");
                }
                // Merge rpcAddressList to addressList and de-duplicate
                synchronized (addressList) {
                    rpcAddressList.forEach(a -> {
                        if (!addressList.contains(a)) addressList.add(a);
                    });
                }

                return rpcAddressList;
            };

            // Submit the callable task to the executor service
            Future<List<String>> future = executor.submit(callable);
            futures.add(future);
        }

        // Shutdown the executor service and wait for all tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        // Process the results of the completed tasks
        for (Future<List<String>> future : futures) {
            try {
                var rpcAddressList = future.get();
                System.out.println("rpcAddressList.size(): " + rpcAddressList.size());
                // Update the status and message of the corresponding LogEth object
                logEthRepository.saveAll(ethBatch);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println("SQLIntegrityConstraintViolationException occurred");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Create Address objects and save them to the database
        for (String addressString : addressList) {
            Address address = new Address();
            address.setAddress(addressString);
            try {
                addressService.saveAddress(address);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println(addressString);
                }
            }
        }
    }

}
