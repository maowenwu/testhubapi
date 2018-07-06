package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.OkMarketService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFutureKlineJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        OkMarketService okMarketService = ApplicationContextHolder.getContext().getBean(OkMarketService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            okMarketService.updateOkFutureKline(jobFuture.getSymbol(), "1min", jobFuture.getContractType());
        }
    }
}
