package com.huobi.quantification.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String hash(String input) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("获取MD5实例异常", e);
        }
        m.update(input.getBytes(), 0, input.length());
        return new BigInteger(1, m.digest()).toString(16).toUpperCase();
    }
}