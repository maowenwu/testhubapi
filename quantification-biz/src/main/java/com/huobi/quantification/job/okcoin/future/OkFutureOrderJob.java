package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.order.OkOrderService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFutureOrderJob extends AbstractQuartzJob {

    @Override
    public void execute(JobParamDto data) {
        OkOrderService okOrderService = ApplicationContextHolder.getContext().getBean(OkOrderService.class);
        okOrderService.updateOkOrderInfo(data.getAccountId(), data.getSymbol(), data.getContractType());
    }
}
