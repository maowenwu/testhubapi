package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.OkAccountService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        OkAccountService okAccountService = ApplicationContextHolder.getContext().getBean(OkAccountService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            okAccountService.updateOkPosition(jobFuture.getAccountId(), jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
