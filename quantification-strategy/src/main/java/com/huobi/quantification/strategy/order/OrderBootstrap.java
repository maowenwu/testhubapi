package com.huobi.quantification.strategy.order;


import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StrategyProperties strategyProperties;
    @Autowired
    private JobManageService jobManageService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            logger.info("==>spring 容器启动");
            StrategyProperties.ConfigGroup group1 = strategyProperties.getGroup1();
            if (group1.getEnable()) {
                startWithConfig(group1);
            }
            StrategyProperties.ConfigGroup group2 = strategyProperties.getGroup2();
            if (group2.getEnable()) {
                startWithConfig(group2);
            }
            StrategyProperties.ConfigGroup group3 = strategyProperties.getGroup3();
            if (group3.getEnable()) {
                startWithConfig(group3);
            }
        }
    }

    private void startWithConfig(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config future = group.getFuture();
        StrategyProperties.Config spot = group.getSpot();
        jobManageService.addHuobiSpotDepthJob(spot.getBaseCoin() + spot.getQuotCoin(), "step1", "0/1 * * * * ?", true);
        jobManageService.addHuobiSpotCurrentPriceJob(spot.getBaseCoin() + spot.getQuotCoin(), "0/1 * * * * ?", true);
        jobManageService.addHuobiFuturePositionJob(future.getAccountId(), "0/1 * * * * ?", true);
        jobManageService.addHuobiFutureUserInfoJob(future.getAccountId(), "0/1 * * * * ?", true);

        OrderCopy orderCopy = ApplicationContextHolder.getContext().getBean(OrderCopy.class);
        orderCopy.init(group);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    orderCopy.copyOrder();
                } catch (Throwable e) {
                    logger.error("拷贝订单期间出现异常", e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {

                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
