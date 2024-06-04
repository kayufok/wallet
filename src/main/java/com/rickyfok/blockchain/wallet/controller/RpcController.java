package com.rickyfok.blockchain.wallet.controller;


import com.rickyfok.blockchain.wallet.service.RpcApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rpc-api")
public class RpcController {

    @Autowired
    RpcApiService RpcApiService;

    //a get method with endpoint "/eth" to call RpcApiService.getApiResponse

    @RequestMapping("/{blockchain}/{fromBlock}/{toBlock}")
    public ResponseEntity<Object> getApiResponse(@PathVariable  String blockchain, @PathVariable long fromBlock, @PathVariable long toBlock) {
        return ResponseEntity.ok(RpcApiService.getApiResponse(blockchain,fromBlock,toBlock));
    }
}
