package com.huobi.quantification.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class AsyncUtils {

    private static Logger logger = LoggerFactory.getLogger(AsyncUtils.class);

    public static CompletableFuture runAsyncNoException(Runnable runnable) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                logger.error("异步任务异常：", e);
            }
        });
        return future;
    }
}
