package com.huobi.quantification.config;

import com.huobi.quantification.quartz.LogJobListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactory schedulerFactory() {
        return new StdSchedulerFactory();
    }

    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) {

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.getListenerManager().addJobListener(logJobListener());
            return schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JobListener logJobListener(){
        return new LogJobListener();
    }
}
