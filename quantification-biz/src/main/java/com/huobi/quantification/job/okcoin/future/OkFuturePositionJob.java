package com.huobi.quantification.job.okcoin.future;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.OkFutureAccountService;

@DisallowConcurrentExecution
public class OkFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        OkFutureAccountService okFutureAccountService = ApplicationContextHolder.getContext().getBean(OkFutureAccountService.class);
        okFutureAccountService.updateOkPosition(data.getAccountId());
    }
}
