package com.rickyfok.blockchain.wallet.scheduler;

import com.rickyfok.blockchain.wallet.service.address.EthereumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnkrRpcScheduler {
    @Autowired
    private EthereumService ethereumService;

//    @Scheduled(fixedRate = 3000)
//    public void logEthereumHandler() {
//        ethereumService.logEthereumHandler().subscribe();
//    }

    @Scheduled(fixedRate = 3000)
    public void logEthereumHandler() {
        ethereumService.processRawRpcResponse();
    }
}
