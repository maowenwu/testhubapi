package com.huobi.quantification.strategy.order;


import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.SideEnum;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.entity.DepthBook;
import com.huobi.quantification.strategy.entity.FuturePosition;
import com.huobi.quantification.strategy.enums.OrderActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class OrderCloser {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommContext commContext;


    private AtomicBoolean forceCloseOrderEnable = new AtomicBoolean(false);

    public void startForceCloseOrder() {
        // 如果正在强平则忽略
        if (!forceCloseOrderEnable.get()) {
            forceCloseOrderEnable.set(true);
            int failedCount = 0;
            while (forceCloseOrderEnable.get()) {
                boolean b = closeOrder();
                if (!b) {
                    failedCount += 1;
                    if (failedCount > 3) {
                        commContext.cancelAllFutureOrder();
                        failedCount = 0;
                    }
                    ThreadUtils.sleep(1000);
                } else {
                    failedCount = 0;
                }
            }
        } else {
            logger.info("当前火币期货正在强平仓位");
        }
    }

    private boolean closeOrder() {
        try {
            OrderActionEnum orderAction = commContext.getOrderAction();
            if (orderAction == OrderActionEnum.STOP_FORCE_CLOSE_ORDER) {
                StrategyOrderConfig orderConfig = commContext.getStrategyOrderConfig();
                if (orderConfig == null) {
                    logger.error("获取期货订单参数配置异常");
                    return false;
                }
                StrategyRiskConfig riskConfig = commContext.getStrategyRiskConfig();
                if (riskConfig == null) {
                    logger.error("获取风控参数配置异常");
                    return false;
                }
                FuturePosition position = commContext.getFuturePosition();
                if (position == null) {
                    logger.error("获取期货持仓异常");
                    return false;
                }
                DepthBook depthBook = commContext.getFutureDepth();
                if (depthBook == null) {
                    logger.error("获取期货DepthBook异常");
                    return false;
                }
                FuturePosition.Position longPosi = position.getLongPosi();
                FuturePosition.Position shortPosi = position.getShortPosi();
                BigDecimal ask1 = depthBook.getAsk1();
                BigDecimal bid1 = depthBook.getBid1();
                boolean b = commContext.cancelAllFutureOrder();
                if (b) {
                    // 如果撤单后保证金率恢复正常直接退出强平逻辑
                    BigDecimal riskRate = commContext.getRiskRate();
                    if (BigDecimalUtils.moreThan(riskRate, riskConfig.getRiskRateLevel3())) {
                        forceCloseOrderEnable.set(false);
                        return false;
                    }
                } else {
                    logger.error("获取期货所有订单异常");
                    return false;
                }
                if (longPosi == null && shortPosi == null) {
                    // 如果多空仓都为空，那么直接退出强平循环
                    forceCloseOrderEnable.set(false);
                    return false;
                } else if (longPosi == null) {
                    // 如果多仓为空，空仓不为空，那么下平空单
                    if (ask1 != null) {
                        BigDecimal orderAmount = BigDecimalUtils.min(orderConfig.getMaxCloseAmount(), shortPosi.getAvailable());
                        BigDecimal orderPrice = ask1.multiply(BigDecimal.ONE.add(orderConfig.getSellCloseSlippage()));
                        placeCloseShortOrder(orderPrice, orderAmount);
                    } else {
                        logger.error("卖一价为空，忽略本笔平空单");
                    }
                } else if (shortPosi == null) {
                    // 如果多仓不为空，空仓为空，那么下平多单
                    if (bid1 != null) {
                        BigDecimal orderAmount = BigDecimalUtils.min(orderConfig.getMaxCloseAmount(), longPosi.getAvailable());
                        BigDecimal orderPrice = bid1.multiply(BigDecimal.ONE.subtract(orderConfig.getBuyCloseSlippage()));
                        placeCloseLongOrder(orderPrice, orderAmount);
                    } else {
                        logger.error("买一价为空，忽略本笔平多单");
                    }
                } else {
                    // 多仓空仓都不为空，那么按大小下单
                    if (BigDecimalUtils.moreThanOrEquals(longPosi.getAvailable(), shortPosi.getAvailable())) {
                        // 如果当前持仓多仓>=空仓，则下平多订单
                        if (bid1 != null) {
                            BigDecimal orderAmount = BigDecimalUtils.min(orderConfig.getMaxCloseAmount(), longPosi.getAvailable());
                            BigDecimal orderPrice = bid1.multiply(BigDecimal.ONE.subtract(orderConfig.getBuyCloseSlippage()));
                            placeCloseLongOrder(orderPrice, orderAmount);
                        } else {
                            logger.error("买一价为空，忽略本笔平多单");
                        }
                    } else {
                        // 则下平空订单
                        if (ask1 != null) {
                            BigDecimal orderAmount = BigDecimalUtils.min(orderConfig.getMaxCloseAmount(), shortPosi.getAvailable());
                            BigDecimal orderPrice = ask1.multiply(BigDecimal.ONE.add(orderConfig.getSellCloseSlippage()));
                            placeCloseShortOrder(orderPrice, orderAmount);
                        } else {
                            logger.error("卖一价为空，忽略本笔平空单");
                        }
                    }
                }
                ThreadUtils.sleep(orderConfig.getCloseOrderInterval() * 1000);
            } else {
                forceCloseOrderEnable.set(false);
            }
            return true;
        } catch (Exception e) {
            logger.error("强平期间出现异常", e);
            ThreadUtils.sleep(1000);
            return false;
        }
    }


    private void placeCloseLongOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        commContext.placeOrder(SideEnum.SELL.getSideType(), OffsetEnum.SHORT.getOffset(), orderPrice, orderAmount);
    }


    private void placeCloseShortOrder(BigDecimal orderPrice, BigDecimal orderAmount) {
        commContext.placeOrder(SideEnum.BUY.getSideType(), OffsetEnum.SHORT.getOffset(), orderPrice, orderAmount);
    }
}
