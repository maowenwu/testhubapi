package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.ContractCodeDto;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RiskBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StrategyProperties strategyProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
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
        RiskMonitor riskMonitor = ApplicationContextHolder.getContext().getBean(RiskMonitor.class);
        riskMonitor.init(group);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    riskMonitor.check();
                } catch (Throwable e) {
                    logger.error("监控保证金率期间出现异常", e);
                    sleep(5000);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {

        }
    }


}
