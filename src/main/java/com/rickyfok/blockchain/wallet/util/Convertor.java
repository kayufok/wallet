package com.rickyfok.blockchain.wallet.util;

import java.util.function.UnaryOperator;

public final class Convertor {

    public static final UnaryOperator<Long> blockFrom = i -> (i-1) * 30 +1 + 100000;
    public static final UnaryOperator<Long> blockTo = i -> i * 30 + 100000;

}