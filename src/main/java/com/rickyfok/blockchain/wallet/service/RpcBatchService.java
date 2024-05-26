package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.repository.LogEthRepository;
import com.rickyfok.blockchain.wallet.util.RpcBatchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rickyfok.blockchain.wallet.repository.LogEthRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

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



    public void rpcAddressBatchEth (){
        // retrieve next eth batch list by retrieveEthBatchBySize()
        var ethBatch = retrieveEthBatchBySize(10);
        var addressList = new ArrayList<String>();

        for (LogEth logEth : ethBatch) {
            // call rpcApiService.getApiResponse()
            var rpcAddressList = rpcApiService.getApiResponse("eth", RpcBatchUtil.findBlockFrom(logEth.getId()), RpcBatchUtil.findBlockTo(logEth.getId()));

            // if response is null, return
            if (rpcAddressList == null) {
                logEth.setStatusId(2L);
                logEth.setMessage("0 Address");
                logEthRepository.save(logEth);
                return;
            }

            // merge rpcAddressList to addressList and de-duplicate
            rpcAddressList.forEach(a -> {
                if (!addressList.contains(a)) addressList.add(a);
            });

            logEth.setStatusId(2L);
            logEth.setMessage(rpcAddressList.size() + " Address");
            logEthRepository.save(logEth);

        };


        // for each addressList, new Address objects
        for(String addressString : addressList) {
            Address address = new Address();
            address.setAddress(addressString);
            try {
                addressService.saveAddress(address);
            } catch (Exception e){
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println(addressString);
                }else return;
            }
        }

    }

}
