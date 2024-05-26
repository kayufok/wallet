package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.repository.LogEthRepository;
import com.rickyfok.blockchain.wallet.util.RpcBatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class RpcBatchService {

    @Autowired
    LogEthRepository logEthRepository;

    @Autowired
    RpcApiService rpcApiService;

    @Autowired
    AddressService addressService;


    public long findMaxBatchId() {
        Long maxId = logEthRepository.findMaxId();
        if (maxId == null) {
            return 0;
        }
        return maxId;
    }

    public List<LogEth> retrieveEthBatchBySize(int size) {

        // create LogEth objects by size insert into database and return the created LogEth objects
        List<LogEth> ethBatch = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LogEth logEth = new LogEth();
            logEth.setStatusId(1L);
            ethBatch.add(logEth);
        }
        logEthRepository.saveAll(ethBatch);
        return ethBatch;
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
                var rpcAddressList = rpcApiService.getApiResponse("eth", RpcBatchUtil.findBlockFrom(logEth.getId()), RpcBatchUtil.findBlockTo(logEth.getId()));

                // If response is null, update the status and return an empty list
                if (rpcAddressList == null || rpcAddressList.isEmpty()) {
                    logEth.setStatusId(2L);
                    logEth.setMessage("0 Address");
                    return new ArrayList<>();
                }else {
                    logEth.setStatusId(2L);
                    logEth.setMessage(rpcAddressList.size() + " Address");
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
