package com.huobi.quantification.common.util;

public class ThreadUtils {


    public static void sleep10() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }

    public static void sleep(long startMillis, int seconds) {
        try {
            long endTime = System.currentTimeMillis();
            long sleepTime = seconds * 1000 - (endTime - startMillis);
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {

        }
    }
}
