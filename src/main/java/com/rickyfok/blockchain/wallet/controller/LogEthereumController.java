package com.rickyfok.blockchain.wallet.controller;

import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import com.rickyfok.blockchain.wallet.service.address.EthereumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LogEthereumController {

    @Autowired
    private EthereumService ethereumService;

    @PostMapping("/api/log-ethereum")
    public Mono<LogEthereum> handleApiCall() {
        return ethereumService.logEthereumHandler();
    }

}
