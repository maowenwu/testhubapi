package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.HuobiFutureAccountService;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        HuobiFutureAccountService accountService = ApplicationContextHolder.getContext().getBean(HuobiFutureAccountService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            accountService.updateHuobiUserInfo(jobFuture.getAccountId());
        }
    }
}
