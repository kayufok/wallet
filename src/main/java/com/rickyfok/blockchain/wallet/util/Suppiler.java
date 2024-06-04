package com.rickyfok.blockchain.wallet.util;

import java.util.function.Supplier;

public class Suppiler {

    // string convertor
    public static Supplier<String> threadName = () -> Thread.currentThread().getName();
}
