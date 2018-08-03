package com.huobi.quantification.job.huobi.spot;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiAccountService;

@DisallowConcurrentExecution
public class HuobiAccountJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        HuobiAccountService accountService = ApplicationContextHolder.getContext().getBean(HuobiAccountService.class);
        accountService.updateAccount(data.getAccountId());
    }

}
