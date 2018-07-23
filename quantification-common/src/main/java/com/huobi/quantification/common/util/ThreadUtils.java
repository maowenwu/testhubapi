package com.huobi.quantification.common.util;

public class ThreadUtils {


    public static void sleep10() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
