package com.rickyfok.blockchain.wallet.util;

import java.util.function.UnaryOperator;

public final class Convertor {

    // long convertor
    public static final UnaryOperator<Long> blockFrom = i -> (i-1) * 10 +1 + 100000;
    public static final UnaryOperator<Long> blockTo = i -> i * 10 + 100000;
    public static final UnaryOperator<String> stringLength500 = i -> i.substring(0, 500);

}