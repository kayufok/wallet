package com.rickyfok.blockchain.wallet.controller;

import com.rickyfok.blockchain.wallet.entity.LogEth;
import com.rickyfok.blockchain.wallet.service.RpcBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rpc/batch")
public class RpcBatchController {

    @Autowired
    RpcBatchService rpcBatchService;

    @GetMapping("max-batch-id")
    public long findMaxId() {
        return rpcBatchService.findMaxBatchId().orElse(0L);
    }

    @PostMapping("eth-batch-interval/{size}")
    public List<LogEth> retrieveEthBatchBySize(@PathVariable int size) {
        return rpcBatchService.retrieveEthBatchBySize(size);
    }

    @PostMapping("eth-batch/run")
    public ResponseEntity<Object> runEthBatch() {
        rpcBatchService.rpcAddressBatchEthStream();
        return ResponseEntity.ok("eth-batch/run");
    }

}
