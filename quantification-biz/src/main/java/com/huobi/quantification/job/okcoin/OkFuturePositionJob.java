package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class OkFuturePositionJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        AccountService accountService = ApplicationContextHolder.getContext().getBean(AccountService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            accountService.updateOkPosition(jobFuture.getAccountId(), jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
