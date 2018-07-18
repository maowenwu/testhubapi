package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiFutureAccountService;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        HuobiFutureAccountService accountService = ApplicationContextHolder.getContext().getBean(HuobiFutureAccountService.class);
        accountService.updateHuobiUserInfo(data.getAccountId());
    }
}
