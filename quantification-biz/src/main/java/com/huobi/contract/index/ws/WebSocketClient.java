package com.huobi.contract.index.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.*;


public abstract class WebSocketClient implements ConnectionStatus {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 重连时间间隔
     */
    private static long RECONN_INTERVAL = 3000;

    /**
     * 心跳时间间隔
     */
    private static long HEARTBEAT_INTERVAL = 1000;

    private static int threadPoolSize = 3;

    private String uri;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadPoolSize, new ThreadFactoryImpl());

    private WebSocketContainer conmtainer = ContainerProvider.getWebSocketContainer();

    private Endpoint endpoint = new Endpoint();

    private boolean reading;
    private boolean reconnecting = true;
    private long readerIdleTimeMilli = 30 * 1000L;
    private long lastReadTime;
    private ScheduledFuture<?> readerIdleTimeout;
    private ScheduledFuture<?> heartbeatFuture;
    private ScheduledFuture<?> reconnectFuture;
    private Session session;

    public WebSocketClient() {
    }

    public WebSocketClient(String uri) {
        Objects.requireNonNull(uri);
        this.uri = uri;
    }


    public void connect() {
        try {
            conmtainer.connectToServer(endpoint, new URI(uri));
            reconnecting = false;
        } catch (Exception e) {
            throw new RuntimeException("WebSocketClient 连接失败", e);
        }
    }

    public void reconnect() {
        reconnecting = true;
        destroy();
        reconnectFuture = schedule(new ReconnectTask(), RECONN_INTERVAL, TimeUnit.MILLISECONDS);
    }

    protected void handleOpen() {

    }

    protected void handleMessage(String message) {

    }

    protected void handleMessage(byte[] message) {
        logger.info("byte[]:" + message);
    }

    public void send(Object message) {
        this.session.getAsyncRemote().sendObject(message);
    }

    public void close() {
        if (this.session != null && this.session.isOpen()) {
            try {
                this.session.close();
            } catch (IOException e) {
                logger.error("websocket close error", e);
            }
        }
    }

    private void ping() throws IOException {
        this.session.getAsyncRemote().sendPing(ByteBuffer.allocate(1));
        logger.trace("heartbeat==>ping");
    }

    @ClientEndpoint
    public class Endpoint {

        @OnOpen
        public void open(Session session) {
            WebSocketClient.this.session = session;
            initialize();
            handleOpen();
        }


        @OnMessage
        public void echoPongMessage(Session session, PongMessage pm) {
            logger.trace("heartbeat<==pong");
            lastReadTime = ticksInMillis();
        }


        @OnMessage
        public void onMessage(byte[] message) {
            reading = true;
            handleMessage(message);
            reading = false;
        }

        @OnMessage
        public void onMessage(String message) {
            reading = true;
            try {
                handleMessage(message);
            } catch (Throwable t) {
                logger.error("websocket 消息处理异常", t);
            }
            reading = false;
        }

        @OnClose
        public void onClose() {
            if (!reconnecting) {
                logger.error("WebSocketClient onClose 准备进行重连");
                reconnect();
            }
        }

        @OnError
        public void onError(Session session, Throwable t) {
            logger.error("WebSocketClient onError occur", t);
        }

    }


    private void initialize() {
        heartbeat();
        lastReadTime = ticksInMillis();
        schedule(new ReaderIdleTimeoutTask(), readerIdleTimeMilli, TimeUnit.MILLISECONDS);
    }

    long ticksInMillis() {
        return System.currentTimeMillis();
    }

    ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return executor.schedule(task, delay, unit);
    }

    private void heartbeat() {
        heartbeatFuture = executor.scheduleAtFixedRate(new HeartbeatTask(), 15 * 1000L, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void destroy() {
        if (readerIdleTimeout != null) {
            readerIdleTimeout.cancel(false);
            readerIdleTimeout = null;
        }
        if (heartbeatFuture != null) {
            heartbeatFuture.cancel(false);
            heartbeatFuture = null;
        }
        if (reconnectFuture != null) {
            reconnectFuture.cancel(false);
            reconnectFuture = null;
        }
        close();
    }

    private final class ReaderIdleTimeoutTask implements Runnable {

        @Override
        public void run() {
            long nextDelay = readerIdleTimeMilli;
            if (!reading) {
                nextDelay -= ticksInMillis() - lastReadTime;
            }

            if (nextDelay <= 0) {
                // Reader is idle - set a new timeout and notify the callback.
                readerIdleTimeout = schedule(this, readerIdleTimeMilli, TimeUnit.MILLISECONDS);
                if (!reconnecting) {
                    logger.error("read timeout 开始重连");
                    reconnect();
                }
            } else {
                // Read occurred before the timeout - set a new timeout with shorter delay.
                readerIdleTimeout = schedule(this, nextDelay, TimeUnit.MILLISECONDS);
            }
        }
    }

    private final class HeartbeatTask implements Runnable {

        @Override
        public void run() {
            try {
                if (!reconnecting) {
                    ping();
                }
            } catch (Throwable t) {
                logger.error("WebSocketClient 心跳异常", t);
            }
        }
    }

    private final class ReconnectTask implements Runnable {
        @Override
        public void run() {
            try {
                connect();
                reconnecting = false;
                logger.error("WebSocketClient重连完成");
            } catch (Throwable t) {
                logger.error("reconnect异常，" + RECONN_INTERVAL + "毫秒后进行重试");
                schedule(this, RECONN_INTERVAL, TimeUnit.MILLISECONDS);
            }
        }
    }

    private final class ThreadFactoryImpl implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("WebSocketClient_thread");
            thread.setDaemon(true);
            return thread;
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean isReconnecting() {
        return reconnecting;
    }
}
