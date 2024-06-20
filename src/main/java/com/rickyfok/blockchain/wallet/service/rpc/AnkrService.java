package com.rickyfok.blockchain.wallet.service.rpc;

import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksRequest;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import com.rickyfok.blockchain.wallet.model.ankr.Params;
import com.rickyfok.blockchain.wallet.util.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import static com.rickyfok.blockchain.wallet.util.Convertor.blockFrom;
import static com.rickyfok.blockchain.wallet.util.Convertor.blockTo;

@Service
public class AnkrService {

    @Value("${rpc-api.endpoint}")
    private String rpcEndpoint;

    private final WebClient webClient;

    public AnkrService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://rpc.ankr.com").build();
    }

    public Mono<String> ankrGetBlocksRequestToString (Long logEthereumId) {
        var GetBlocksRequest = prepareGetBlocksRequest(logEthereumId);
        return this.webClient
                .post()
                .uri(rpcEndpoint)
                .bodyValue(prepareGetBlocksRequest(logEthereumId))
                .retrieve()
                .bodyToMono(String.class);
    }

    private GetBlocksRequest prepareGetBlocksRequest (Long logEthereumId){
        var params = prepareRequestParams(logEthereumId);
        return new GetBlocksRequest(
                Constant.ANKR_JSON_RPC_VERSION,
                Constant.ANKR_API_GET_BLOCKS,
                params,
                logEthereumId
        );
    }

    private Params prepareRequestParams (Long logEthereumId) {
        return new Params(
                Constant.ETHEREUM_CODE
                , blockFrom.apply(logEthereumId)
                , blockTo.apply(logEthereumId)
                , false
                , true
                , true
                , true
        );
    }
}
