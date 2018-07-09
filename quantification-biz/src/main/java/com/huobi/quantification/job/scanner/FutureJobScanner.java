package com.huobi.quantification.job.scanner;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.job.JobService;
import org.quartz.DisallowConcurrentExecution;

/**
 * 期货job定时扫描更新
 */
@DisallowConcurrentExecution
public class FutureJobScanner extends AbstractQuartzJob {
    @Override
    public void execute(Object data) {
        JobService jobService = ApplicationContextHolder.getContext().getBean(JobService.class);
        jobService.updateFutureJobScheduler();
    }
}
