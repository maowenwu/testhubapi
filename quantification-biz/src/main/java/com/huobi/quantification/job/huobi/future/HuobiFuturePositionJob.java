package com.huobi.quantification.job.huobi.future;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.FutureAccountService;

@DisallowConcurrentExecution
public class HuobiFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        FutureAccountService futureAccountService = ApplicationContextHolder.getContext().getBean(FutureAccountService.class);
        futureAccountService.updateHuobiPosition(data.getAccountId());
    }
}
