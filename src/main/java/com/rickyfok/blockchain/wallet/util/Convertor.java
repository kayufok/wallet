package com.rickyfok.blockchain.wallet.util;

import java.util.function.UnaryOperator;

import static com.rickyfok.blockchain.wallet.util.Constant.ankrGetBlockSize;

public final class Convertor {

    // long convertor
    public static final UnaryOperator<Long> blockFrom = i -> (i-1) * ankrGetBlockSize +1;
    public static final UnaryOperator<Long> blockTo = i -> i * ankrGetBlockSize;

}