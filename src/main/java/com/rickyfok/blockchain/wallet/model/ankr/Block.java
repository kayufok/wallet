package com.rickyfok.blockchain.wallet.model.ankr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    private String blockchain;
    private String number;
    private String hash;
    private String parentHash;
    private String nonce;
    private String mixHash;
    private String sha3Uncles;
    private String logsBloom;
    private String stateRoot;
    private String miner;
    private String difficulty;
    private String extraData;
    private String size;
    private String gasLimit;
    private String gasUsed;
    private String timestamp;
    private String transactionsRoot;
    private String receiptsRoot;
    private String totalDifficulty;
    private List<Transaction> transactions;
}
