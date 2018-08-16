package com.huobi.quantification.strategy;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.common.util.ThreadUtils;
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
    @Autowired
    private CommContext commContext;

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
                commContext.init(group);
                // percent10
                jobManageService.addHuobiSpotDepthJob(spot.getBaseCoin() + spot.getQuotCoin(), "step0", "0/1 * * * * ?", true);
                jobManageService.addHuobiSpotCurrentPriceJob(spot.getBaseCoin() + spot.getQuotCoin(), "0/1 * * * * ?", true);
                jobManageService.addHuobiSpotAccountJob(spot.getAccountId(), "0/1 * * * * ?", true);

                jobManageService.addHuobiFuturePositionJob(future.getAccountId(), "0/1 * * * * ?", true);
                jobManageService.addHuobiFutureUserInfoJob(future.getAccountId(), "0/1 * * * * ?", true);
                jobManageService.addHuobiFutureContractCodeJob("0/10 * * * * ?", true);
                jobManageService.addHuobiFutureDepthJob(future.getBaseCoin() + "_" + future.getQuotCoin(), commContext.getContractTypeFromCode(), "step0", "0/1 * * * * ?", true);
                logger.info("注册job完成");
                // 等待3秒，保证job已经完全运行
                ThreadUtils.sleep(3000);
                //startOrderCopierWithConfig(group);
                startHedgerWithConfig(group);
                //startRiskMonitorWithConfig(group);
            }
        }
    }

    private void startOrderCopierWithConfig(StrategyProperties.ConfigGroup group) {
        logger.info("准备启动借深度线程，当前配置：{}", JSON.toJSONString(group));
        OrderCopier orderCopier = ApplicationContextHolder.getContext().getBean(OrderCopier.class);
        logger.info("初始化OrderCopy开始");
        orderCopier.init(group);
        logger.info("初始化OrderCopy完成");
        orderCopier.start();
        logger.info("合约借深度线程启动...");
    }

    private void startRiskMonitorWithConfig(StrategyProperties.ConfigGroup group) {
        RiskMonitor riskMonitor = ApplicationContextHolder.getContext().getBean(RiskMonitor.class);
        riskMonitor.init(group);
        riskMonitor.start();
    }


    private void startHedgerWithConfig(StrategyProperties.ConfigGroup group) {
        Hedger hedger = ApplicationContextHolder.getContext().getBean(Hedger.class);
        hedger.init(group);
        hedger.startHedgePhase1();
    }


}
