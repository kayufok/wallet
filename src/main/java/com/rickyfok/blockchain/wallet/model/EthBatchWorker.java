package com.rickyfok.blockchain.wallet.model;

import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthBatchWorker {
    LogEth logEth;
    GetBlocksResponse getBlocksResponse;
}
