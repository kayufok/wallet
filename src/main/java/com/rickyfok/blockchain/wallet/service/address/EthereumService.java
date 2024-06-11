package com.rickyfok.blockchain.wallet.service.address;

import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import com.rickyfok.blockchain.wallet.repository.LogEthereumRepository;
import com.rickyfok.blockchain.wallet.service.rpc.AnkrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EthereumService {

    @Autowired
    private LogEthereumRepository logEthereumRepository;

    @Autowired
    private AnkrService ankrService;


    public Mono<LogEthereum> logEthereumHandler() {

        LogEthereum logEthereum = new LogEthereum();
        logEthereum.setStatusId(1L);
        logEthereum = logEthereumRepository.save(logEthereum);

        return Mono.just(logEthereum)
                .flatMap(this::makeAnkrApiCall)
                .flatMap(this::saveMessage);

    }


    private Mono<LogEthereum> makeAnkrApiCall(LogEthereum logEthereum) {
        return ankrService
                .ankrGetBlocksRequestToString(logEthereum.getId())
                .map(logEthereum::setMessage)
                .map(log -> log.setStatusId(2L))
                .onErrorResume(e -> {
                    logEthereum.setStatusId(99L);
                    logEthereum.setMessage(e.getLocalizedMessage());
                    logEthereumRepository.save(logEthereum);  // Save the updated status code to the database
                    return Mono.error(e);
                });
    }

    private Mono<LogEthereum> saveMessage(LogEthereum logEthereum) {
        logEthereum = logEthereumRepository.save(logEthereum);
        return Mono.just(logEthereum);
    }

}
