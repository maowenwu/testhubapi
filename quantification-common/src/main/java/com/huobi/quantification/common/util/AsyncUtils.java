package com.huobi.quantification.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class AsyncUtils {

    private static Logger logger = LoggerFactory.getLogger(AsyncUtils.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(200, new ThreadFactory() {
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
        }, executor);
        return future;
    }

    public static <T> Future<T> submit(Callable<T> task) {
        Future<T> future = executor.submit(task);
        return future;
    }

    /**
     * 方法超时后会设置中断标志，使用该方法需要自行处理中断
     *
     * @param supplier
     * @param milliseconds 超时时间 单位毫秒
     * @param <U>
     * @return
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public static <U> U supplyAsync(Supplier<U> supplier, long milliseconds) throws ExecutionException, TimeoutException {
        Future<U> future = executor.submit(() -> supplier.get());
        U u = null;
        try {
            u = future.get(milliseconds, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("线程{}中断异常：", Thread.currentThread().getName(), e);
        } catch (ExecutionException e) {
            throw e;
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        }
        return u;
    }
}
