package com.huobi.quantification.job.huobi.future;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiFutureAccountService;

@DisallowConcurrentExecution
public class HuobiFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        HuobiFutureAccountService accountService = ApplicationContextHolder.getContext().getBean(HuobiFutureAccountService.class);
        accountService.updateHuobiUserInfo(data.getAccountId());
    }
}
