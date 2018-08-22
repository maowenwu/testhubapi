package com.huobi.quantification.init;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.job.scanner.FutureJobScanner;
import com.huobi.quantification.job.scanner.SpotJobScanner;
import com.huobi.quantification.quartz.QuartzManager;
import com.huobi.quantification.service.account.FutureAccountService;
import com.huobi.quantification.service.account.SpotAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuartzManager quartzManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            quartzManager.addJobNoRepeat("FutureJobScanner", FutureJobScanner.class, "0/1 * * * * ?", null);
            quartzManager.addJobNoRepeat("SpotJobScanner", SpotJobScanner.class, "0/1 * * * * ?", null);
            logger.info("注册JobScanner完成");
            SpotAccountService spotAccountService = ApplicationContextHolder.getContext().getBean(SpotAccountService.class);
            spotAccountService.initSpotAccountAsset();
            logger.info("初始化SpotAccountAsset完成");
            FutureAccountService futureAccountService = ApplicationContextHolder.getContext().getBean(FutureAccountService.class);
            futureAccountService.initFutureAccountAsset();
            logger.info("初始化FutureAccountAsset完成");
        }
    }
}
