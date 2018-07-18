package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        OkFutureAccountService okFutureAccountService = ApplicationContextHolder.getContext().getBean(OkFutureAccountService.class);
        okFutureAccountService.updateOkUserInfo(data.getAccountId());
    }
}
