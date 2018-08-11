package com.huobi.quantification.strategy.order;


import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.entity.DepthBook;
import com.huobi.quantification.strategy.entity.FuturePosition;
import com.huobi.quantification.strategy.enums.OrderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class OrderCloser {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;

    private Thread forceCloseOrderThread;

    private AtomicBoolean forceCloseOrderEnable = new AtomicBoolean(false);

    public void startForceCloseOrder() {
        // 如果正在强平则忽略
        if (!forceCloseOrderEnable.get()) {
            forceCloseOrderEnable.set(true);
            forceCloseOrderThread = new Thread(() -> {
                OrderActionEnum orderAction = commContext.getOrderAction();
                StrategyOrderConfig orderConfig = commContext.getStrategyOrderConfig();
                FuturePosition futurePosition = commContext.getFuturePosition();
                while (forceCloseOrderEnable.get() && orderAction == OrderActionEnum.STOP_FORCE_CLOSE_ORDER) {
                    commContext.cancelAllFutureOrder();
                    DepthBook depthBook = commContext.getFutureDepth();
                }
                forceCloseOrderEnable.set(false);
            });
            forceCloseOrderThread.setDaemon(true);
            forceCloseOrderThread.setName("火币期货强平线程");
            forceCloseOrderThread.start();
        } else {
            logger.info("当前火币期货正在强平仓位");
        }
    }
}
