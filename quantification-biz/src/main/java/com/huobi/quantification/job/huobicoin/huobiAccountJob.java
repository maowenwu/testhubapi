package com.huobi.quantification.job.huobicoin;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.AccountHuobiService;

@DisallowConcurrentExecution
public class huobiAccountJob extends AbstractQuartzJob{

	@Override
	public void execute(Object data) {
		AccountHuobiService bean = ApplicationContextHolder.getContext().getBean(AccountHuobiService.class);
		if (data instanceof QuanJob) {
			QuanJob jobData = (QuanJob)data;
			bean.accounts(String.valueOf(jobData.getAccountId()));
		}
	}

}
