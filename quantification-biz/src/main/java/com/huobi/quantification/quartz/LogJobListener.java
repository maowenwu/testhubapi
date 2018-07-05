package com.huobi.quantification.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

public class LogJobListener implements JobListener {

    @Override
    public String getName() {
        return "LogJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        long startTime = System.currentTimeMillis();
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        map.put("startTime", startTime);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobDetail.getKey();
    }
}
