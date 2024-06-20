package com.rickyfok.blockchain.wallet.service.address;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickyfok.blockchain.wallet.entity.AddressTemporary;
import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import com.rickyfok.blockchain.wallet.model.EthereumMessage;
import com.rickyfok.blockchain.wallet.model.ankr.Block;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import com.rickyfok.blockchain.wallet.model.ankr.Result;
import com.rickyfok.blockchain.wallet.model.ankr.Transaction;
import com.rickyfok.blockchain.wallet.repository.AddressTemporaryRepository;
import com.rickyfok.blockchain.wallet.repository.LogEthereumRepository;
import com.rickyfok.blockchain.wallet.service.rpc.AnkrService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.rickyfok.blockchain.wallet.util.Validator.isEmptyList;
import static com.rickyfok.blockchain.wallet.util.Validator.isValidList;

@Service
@Log4j2
public class EthereumService {

    @Autowired
    private LogEthereumRepository logEthereumRepository;

    @Autowired
    private AddressTemporaryRepository addressTemporaryRepository;

    @Autowired
    private AnkrService ankrService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public Mono<LogEthereum> logEthereumHandler() {

        LogEthereum logEthereum = new LogEthereum();
        logEthereum.statusId(1L);
        logEthereum = logEthereumRepository.save(logEthereum);

        return Mono.just(logEthereum)
                .flatMap(this::makeAnkrApiCall)
                .flatMap(this::saveMessage);

    }


    private Mono<LogEthereum> makeAnkrApiCall(LogEthereum logEthereum) {
        return ankrService
                .ankrGetBlocksRequestToString(logEthereum.id())
                .map(logEthereum::message)
                .map(log -> log.statusId(2L))
                .onErrorResume(e -> {
                    logEthereum.statusId(99L);
                    logEthereum.message(e.getLocalizedMessage());
                    logEthereumRepository.save(logEthereum);  // Save the updated status code to the database
                    return Mono.error(e);
                });
    }

    private Mono<LogEthereum> saveMessage(LogEthereum logEthereum) {
        logEthereum = logEthereumRepository.save(logEthereum);
        return Mono.just(logEthereum);
    }

    public void processRawRpcResponse(){
        log.info("Processing : {}", new Date());
        var logEthereumList = getLogsByStatusId(1);
        
        if (isEmptyList.test(logEthereumList)) {
            log.info("no records");
            return;
        }

        var ethereumMessageList = logEthereumList.stream()
                .map(e -> new EthereumMessage(null,e))
                .map(this::getResponseFromMessage)
                .filter(e -> e.getBlocksResponse() != null)
                .toList();

        var addressTemporaryList = ethereumMessageList.stream()
                .map(EthereumMessage::getBlocksResponse)
                .map(GetBlocksResponse::getResult)
                .filter(Objects::nonNull)
                .map(Result::getBlocks)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(Block::getTransactions)
                .filter(Objects::nonNull)
                .flatMap(tx -> Stream.concat(tx.stream().map(Transaction::getFrom),tx.stream().map(Transaction::getTo)))
                .filter(Objects::nonNull)
                .distinct()
                .map(a -> new AddressTemporary(null,a))
                .toList();

        addressTemporaryRepository.saveAll(addressTemporaryList);

    }

    private EthereumMessage getResponseFromMessage (EthereumMessage ethereumMessage){

        try {

            String message = ethereumMessage.logEthereum().message();
            GetBlocksResponse getBlocksResponse = objectMapper.readValue(message, GetBlocksResponse.class);
            ethereumMessage.getBlocksResponse(getBlocksResponse);
            logEthereumRepository.save(ethereumMessage.logEthereum().statusId(3L));
            return ethereumMessage;
        } catch (Exception e) {
            logEthereumRepository.save(ethereumMessage.logEthereum().statusId(98L).message(e.getMessage()));
            ethereumMessage.getBlocksResponse(null);
            return ethereumMessage;
        }

    }

    public List<LogEthereum> getLogsByStatusId(int statusId) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        return logEthereumRepository.findByStatusId(statusId, pageRequest);
    }

}
