package com.rickyfok.blockchain.wallet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
public class RpcApiService {

    //variable from application.yaml rpc-api.base-url by using @value annotation
    @Value("${rpc-api.base-url}")
    private String rpcBaseUrl;

    @Value("${rpc-api.port}")
    private String rpcPort;

    @Value("${rpc-api.endpoint}")
    private String rpcEndpoint;

    /*
     * a public function to get the response from api.
     * API url rpcBaseUrl+rpcPort+rpcEndpoint/blockChain/fromBlock/toBlock
     * where blockChain,fromBlock,toBlock are input parameter.
     * the return of API are list of String. when API returns HTTPS CODE 500, return null
     */
    public List<String> getApiResponse(String blockChain, long fromBlock, long toBlock) {

        WebClient webClient = WebClient.builder().baseUrl(rpcBaseUrl+":"+rpcPort).build();

        Mono<List<String>> responseMono = webClient
                .get().uri(rpcEndpoint+ "/" + blockChain + "/" + fromBlock + "/" + toBlock)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});

        return responseMono.block();

    }

}
