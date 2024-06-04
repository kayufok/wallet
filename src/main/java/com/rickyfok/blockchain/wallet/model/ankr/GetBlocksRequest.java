package com.rickyfok.blockchain.wallet.model.ankr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetBlocksRequest {
    private String jsonrpc;
    private String method;
    private Params params;
    private Long id;
}