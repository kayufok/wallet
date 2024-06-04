package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.model.EthBatchWorker;
import com.rickyfok.blockchain.wallet.model.ankr.Block;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import com.rickyfok.blockchain.wallet.model.ankr.Result;
import com.rickyfok.blockchain.wallet.model.ankr.Transaction;
import com.rickyfok.blockchain.wallet.repository.LogEthRepository;
import com.rickyfok.blockchain.wallet.util.Suppiler;
import com.rickyfok.blockchain.wallet.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.rickyfok.blockchain.wallet.util.Convertor.*;

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

        // batch initialization
        var ethBatch = retrieveEthBatchBySize(1);

        // early break when batch is empty
        if (Validator.isEmptyList.test(ethBatch)) return;

        var ethBatchWorkerList = ethBatch.stream()
                .parallel()
                .peek(b -> System.out.println(Suppiler.threadName.get() + "batch id:" + b.getId()))
                .map(b -> {
                    try {
                        return new EthBatchWorker(b.setStatusId(2L).setMessage("Success"),rpcApiService.getAnkrGetBlocks("eth", blockFrom.apply(b.getId()), blockTo.apply(b.getId()),b.getId()).block());
                    }catch (Exception e) {
                        return new EthBatchWorker(b.setStatusId(3L).setMessage(stringLength500.apply(e.getLocalizedMessage())), new GetBlocksResponse());
                    }
                })
                .peek(w -> logEthRepository.save(w.getLogEth()))
                .flatMap(el -> Stream.of(el.getGetBlocksResponse()))
                .toList();


        var addressList = ethBatchWorkerList.stream()
                .parallel()
                .map(GetBlocksResponse::getResult)
                .map(Result::getBlocks)
                .flatMap(List::stream)
                .map(Block::getTransactions)
                .flatMap(tx -> Stream.concat(tx.stream().map(Transaction::getFrom), tx.stream().map(Transaction::getTo)))
                .distinct()
                .map(s -> new Address().setAddress(s))
                .toList();

        addressList.stream()
                .parallel()
                .filter(a -> addressService.getAddressCount(a.getAddress()) == 0)
                .forEach(a -> addressService.saveAddress(a));

    }


}
