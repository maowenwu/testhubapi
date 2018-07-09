package com.huobi.quantification.init;


import com.huobi.quantification.job.scanner.FutureJobScanner;
import com.huobi.quantification.job.scanner.SpotJobScanner;
import com.huobi.quantification.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private QuartzManager quartzManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            quartzManager.addJobNoRepeat("FutureJobScanner", FutureJobScanner.class, "0/1 * * * * ?", null);
            //quartzManager.addJobNoRepeat("SpotJobScanner", SpotJobScanner.class, "0/1 * * * * ?", null);
        }
    }
}
