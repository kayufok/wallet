package com.rickyfok.blockchain.wallet.model.ankr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBlocksResponse {
    private String jsonrpc;
    private Integer id;
    private Result result;
    private Error error;
}
