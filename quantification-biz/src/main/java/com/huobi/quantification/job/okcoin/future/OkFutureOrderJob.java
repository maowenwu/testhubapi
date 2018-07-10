package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.order.OkOrderService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFutureOrderJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        OkOrderService okOrderService = ApplicationContextHolder.getContext().getBean(OkOrderService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            okOrderService.updateOkOrderInfo(jobFuture.getAccountId(), jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
