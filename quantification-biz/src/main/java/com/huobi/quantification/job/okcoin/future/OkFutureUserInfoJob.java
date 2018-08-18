package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.service.account.FutureAccountService;
import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;

@DisallowConcurrentExecution
public class OkFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        /*FutureAccountService okFutureAccountService = ApplicationContextHolder.getContext().getBean(FutureAccountService.class);
        okFutureAccountService.updateOkUserInfo(data.getAccountId());*/
    }
}
