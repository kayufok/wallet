package com.rickyfok.blockchain.wallet.model;

import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import com.rickyfok.blockchain.wallet.model.ankr.GetBlocksResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class EthereumMessage {
    private GetBlocksResponse getBlocksResponse;
    private LogEthereum logEthereum;
}
