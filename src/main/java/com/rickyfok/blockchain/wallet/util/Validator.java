package com.rickyfok.blockchain.wallet.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;


public class Validator {

    public static final Predicate<List<?>> isEmptyList = CollectionUtils::isEmpty;
    public static final Predicate<List<?>> isValidList = isEmptyList.negate();

}
