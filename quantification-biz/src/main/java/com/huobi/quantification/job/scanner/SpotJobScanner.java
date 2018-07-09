package com.huobi.quantification.job.scanner;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.job.JobService;

/**
 * 现货job定时扫描更新
 */
public class SpotJobScanner extends AbstractQuartzJob {

	@Override
	public void execute(Object data) {
		JobService jobService = ApplicationContextHolder.getContext().getBean(JobService.class);
		jobService.updateJobScheduler();
	}

}
