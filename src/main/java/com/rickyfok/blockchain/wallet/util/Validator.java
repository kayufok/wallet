package com.rickyfok.blockchain.wallet.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;


public class Validator {

    private static final Predicate<List<?>> isEmptyList = CollectionUtils::isEmpty;
    private static final Predicate<List<?>> isValidList = isEmptyList.negate();

}
