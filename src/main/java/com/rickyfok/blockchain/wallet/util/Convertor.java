package com.rickyfok.blockchain.wallet.util;

import java.util.function.UnaryOperator;

import static com.rickyfok.blockchain.wallet.util.Constant.ankrGetBlockSize;

public final class Convertor {

    // long convertor
    public static final UnaryOperator<Long> blockFrom = i -> (i-1) * ankrGetBlockSize +1 + 100000;
    public static final UnaryOperator<Long> blockTo = i -> i * ankrGetBlockSize + 100000;
    public static final UnaryOperator<String> stringLength500 = i -> i.substring(0, 500);

}