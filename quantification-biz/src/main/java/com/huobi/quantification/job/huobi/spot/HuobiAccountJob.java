package com.huobi.quantification.job.huobi.spot;

import com.huobi.quantification.dto.JobParamDto;
import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiAccountService;

@DisallowConcurrentExecution
public class HuobiAccountJob extends AbstractQuartzJob{

	@Override
	public void execute(JobParamDto data) {
		HuobiAccountService bean = ApplicationContextHolder.getContext().getBean(HuobiAccountService.class);
		/*if (data instanceof QuanJob) {
			QuanJob jobData = (QuanJob)data;
			bean.accounts(String.valueOf(jobData.getAccountId()));
		}*/
	}

}
