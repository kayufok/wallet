package com.rickyfok.blockchain.wallet.model.ankr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBlocksResponse {
    private String jsonrpc;
    private Integer id;
    private Result result;
    private Error error;
}
