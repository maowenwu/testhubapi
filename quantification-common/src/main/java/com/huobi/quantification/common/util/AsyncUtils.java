package com.huobi.quantification.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncUtils {

    private static Logger logger = LoggerFactory.getLogger(AsyncUtils.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(200,new ThreadFactory() {
        private final AtomicInteger nextId = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("async-executor-" + nextId.getAndIncrement());
            return thread;
        }
    });

    public static CompletableFuture runAsyncNoException(Runnable runnable) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                logger.error("异步任务异常：", e);
            }
        },executor);
        return future;
    }
}
