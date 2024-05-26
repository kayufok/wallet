package com.rickyfok.blockchain.wallet.util;

public class RpcBatchUtil {
    public static long findBlockFrom (long batchId) {
        return (batchId - 1) * 30 +100000 + 1;
    }

    public static long findBlockTo (long batchId) {
        return batchId * 30 + 100000;
    }
}
