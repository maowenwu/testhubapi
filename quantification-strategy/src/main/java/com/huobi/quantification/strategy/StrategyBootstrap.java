package com.huobi.quantification.strategy;

import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.entity.StrategyInstanceConfig;
import com.huobi.quantification.strategy.hedge.Hedger;
import com.huobi.quantification.strategy.order.OrderCopier;
import com.huobi.quantification.strategy.risk.RiskMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyBootstrap {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JobManageService jobManageService;
    @Autowired
    private CommContext commContext;
    @Autowired
    private OrderCopier orderCopier;
    @Autowired
    private  Hedger hedger;
    @Autowired
    private RiskMonitor riskMonitor;


    public void startInstance(StrategyInstanceConfig config) {
        logger.info("开始启动实例：{}，实例id：{}", config.getStrategyName(), config.getInstanceId());
        commContext.init(config);
        logger.info("注册job开始");
        // percent10
        jobManageService.addHuobiSpotDepthJob(config.getSpotBaseCoin() + config.getSpotQuotCoin(), "step0", "0/1 * * * * ?", true);
        jobManageService.addHuobiSpotCurrentPriceJob(config.getSpotBaseCoin() + config.getSpotQuotCoin(), "0/1 * * * * ?", true);
        jobManageService.addHuobiSpotAccountJob(config.getSpotAccountId(), "0/1 * * * * ?", true);

        jobManageService.addHuobiFuturePositionJob(config.getFutureAccountId(), "0/1 * * * * ?", true);
        jobManageService.addHuobiFutureUserInfoJob(config.getFutureAccountId(), "0/1 * * * * ?", true);
        jobManageService.addHuobiFutureContractCodeJob("0/10 * * * * ?", true);
        jobManageService.addHuobiFutureDepthJob(config.getFutureBaseCoin() + "_" + config.getFutureQuotCoin(), commContext.getContractTypeFromCode(), "step0", "0/1 * * * * ?", true);
        logger.info("注册job完成");
        // 等待3秒，保证job已经完全运行
        ThreadUtils.sleep(3000);
        startOrderCopierWithConfig(config);
        //startHedgerWithConfig(config);
        //startRiskMonitorWithConfig(config);
    }


    private void startOrderCopierWithConfig(StrategyInstanceConfig config) {
        orderCopier.init(config);
        orderCopier.start();
    }

    private void startHedgerWithConfig(StrategyInstanceConfig config) {
        hedger.init(config);
        hedger.startHedgePhase1();
    }

    private void startRiskMonitorWithConfig(StrategyInstanceConfig config) {
        riskMonitor.init(config);
        riskMonitor.start();
    }

    public void stopInstance() {
        orderCopier.stop();
        hedger.stop();
        riskMonitor.stop();
    }
}
