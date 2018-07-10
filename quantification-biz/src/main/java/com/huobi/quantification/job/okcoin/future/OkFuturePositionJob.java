package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.OkFutureAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        OkFutureAccountService okFutureAccountService = ApplicationContextHolder.getContext().getBean(OkFutureAccountService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            okFutureAccountService.updateOkPosition(jobFuture.getAccountId(), jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
