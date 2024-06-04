package com.rickyfok.blockchain.wallet.model.ankr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {
    private String blockchain;
    private Long fromBlock;
    private Long toBlock;
    private Boolean decodeLogs;
    private Boolean decodeTxData;
    private Boolean includeLogs;
    private Boolean includeTxs;
}