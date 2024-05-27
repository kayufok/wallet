package com.rickyfok.blockchain.wallet.util;

public class RpcBatchUtil {

    private static final blockHeightHandler blockFrom = i -> (i-1) * 30 +1;
    private static final blockHeightHandler blockTo = i -> i * 30;

    public static long findBlockFrom (long batchId) {
        return blockFrom.perform(batchId);
    }

    public static long findBlockTo (long batchId) {
        return blockTo.perform(batchId);
    }

}

@FunctionalInterface
interface blockHeightHandler {
    long perform(long i);
}
