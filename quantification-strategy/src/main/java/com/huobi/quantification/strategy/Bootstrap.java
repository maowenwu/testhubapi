package com.huobi.quantification.strategy;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedge.Hedger;
import com.huobi.quantification.strategy.order.OrderCopier;
import com.huobi.quantification.strategy.risk.RiskMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StrategyProperties strategyProperties;
    @Autowired
    private JobManageService jobManageService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            logger.info("spring 容器启动");
            StrategyProperties.ConfigGroup group = strategyProperties.getGroup();
            logger.info("当前配置：{}", JSON.toJSONString(group));
            if (group.getEnable()) {
                logger.info("注册job开始");
                StrategyProperties.Config future = group.getFuture();
                StrategyProperties.Config spot = group.getSpot();
                jobManageService.addHuobiSpotDepthJob(spot.getBaseCoin() + spot.getQuotCoin(), "percent10", "0/1 * * * * ?", true);
                jobManageService.addHuobiSpotCurrentPriceJob(spot.getBaseCoin() + spot.getQuotCoin(), "0/1 * * * * ?", true);
                jobManageService.addHuobiSpotAccountJob(spot.getAccountId(), "0/1 * * * * ?", true);
                jobManageService.addHuobiFuturePositionJob(future.getAccountId(), "0/1 * * * * ?", true);
                jobManageService.addHuobiFutureUserInfoJob(future.getAccountId(), "0/1 * * * * ?", true);
                jobManageService.addHuobiFutureContractCodeJob("0/10 * * * * ?", true);
                logger.info("注册job完成");
                // 等待3秒，保证job已经完全运行
                //sleep(3000);
                contextInit(group);
                //startOrderCopierWithConfig(group);
                startHedgerWithConfig(group);
                //startRiskMonitorWithConfig(group);
            }
        }
    }

    private void contextInit(StrategyProperties.ConfigGroup group) {
        CommContext commContext = ApplicationContextHolder.getContext().getBean(CommContext.class);
        commContext.init(group);
    }

    private void startOrderCopierWithConfig(StrategyProperties.ConfigGroup group) {
        logger.info("准备启动借深度线程，当前配置：{}", JSON.toJSONString(group));
        OrderCopier orderCopier = ApplicationContextHolder.getContext().getBean(OrderCopier.class);
        logger.info("初始化OrderCopy开始");
        orderCopier.init(group);
        logger.info("初始化OrderCopy完成");
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    boolean success = orderCopier.copyOrder();
                    if (!success) {
                        sleep(5000);
                    }
                } catch (Throwable e) {
                    logger.error("拷贝订单期间出现异常", e);
                    sleep(5000);
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("摆单线程");
        thread.start();
        logger.info("合约借深度线程启动...");
    }

    private void startRiskMonitorWithConfig(StrategyProperties.ConfigGroup group) {
        RiskMonitor riskMonitor = ApplicationContextHolder.getContext().getBean(RiskMonitor.class);
        riskMonitor.init(group);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    riskMonitor.check();
                } catch (Throwable e) {
                    logger.error("监控保证金率期间出现异常", e);
                    sleep(3000);
                }
                sleep(3000);
            }
        });
        thread.setDaemon(true);
        thread.setName("风控线程");
        thread.start();
    }


    private void startHedgerWithConfig(StrategyProperties.ConfigGroup group) {
        Hedger hedger = ApplicationContextHolder.getContext().getBean(Hedger.class);
        hedger.init(group);
        hedger.hedgePhase1();
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {

        }
    }


}
