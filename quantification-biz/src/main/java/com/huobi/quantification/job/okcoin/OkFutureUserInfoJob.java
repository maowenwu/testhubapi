package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.AccountService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
public class OkFutureUserInfoJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        AccountService accountService = ApplicationContextHolder.getContext().getBean(AccountService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            accountService.updateOkUserInfo(jobFuture.getAccountId());
        }
    }
}
