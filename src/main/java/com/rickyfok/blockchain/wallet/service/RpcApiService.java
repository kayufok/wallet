package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksRequest;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import com.rickyfok.blockchain.wallet.model.ankr.Params;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
@Service
@Log4j2
public class RpcApiService {

    //variable from application.yaml rpc-api.base-url by using @value annotation
    @Value("${rpc-api.base-url}")
    private String rpcBaseUrl;

    @Value("${rpc-api.port}")
    private String rpcPort;

    @Value("${rpc-api.endpoint}")
    private String rpcEndpoint;

    private final WebClient webClient;

    public RpcApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com/todos/").build();
    }

    public Mono<String> makeRequest(Long logEthereumId) {
        return this.webClient.get()
                .uri("{id}", logEthereumId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public List<String> getApiResponse(String blockChain, long fromBlock, long toBlock) {

        WebClient webClient = WebClient.builder().baseUrl(rpcBaseUrl+":"+rpcPort).build();

        Mono<List<String>> responseMono = webClient
                .get().uri(rpcEndpoint+ "/" + blockChain + "/" + fromBlock + "/" + toBlock)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});

        return responseMono.block();

    }

    public Mono<GetBlocksResponse> getAnkrGetBlocks(String blockChain, long fromBlock, long toBlock, Long requestId) {
        WebClient webClient = WebClient.builder().baseUrl("https://rpc.ankr.com").build();

        var params = new Params(blockChain, fromBlock, toBlock, false, true, true, true);

        GetBlocksRequest request = new GetBlocksRequest(
                "2.0",
                "ankr_getBlocks",
                params,
                requestId
        );

         return webClient
                .post()
                .uri("/multichain/ee19a9a6fe722d2ce427185a1f75db2a4d414461037af02cde80e6012c518799")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GetBlocksResponse>() {})
                 .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(10)));

    }

}
