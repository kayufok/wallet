package com.rickyfok.blockchain.wallet.model.ankr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String v;
    private String r;
    private String s;
    private String nonce;
    private String blockNumber;
    private String from;
    private String to;
    private String gas;
    private String gasPrice;
    private String input;
    private String transactionIndex;
    private String blockHash;
    private String value;
    private String type;
    private String cumulativeGasUsed;
    private String gasUsed;
    private String hash;
    private String status;
    private String blockchain;
    private String timestamp;
}