package com.rickyfok.blockchain.wallet.model;

import com.rickyfok.blockchain.wallet.entity.LogEth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthBatchWorker {
    LogEth logEth;
    List<String> addressList;
}
