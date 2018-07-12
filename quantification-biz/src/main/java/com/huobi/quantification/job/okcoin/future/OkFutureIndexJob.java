package com.huobi.quantification.job.okcoin.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.OkFutureMarketService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class OkFutureIndexJob extends AbstractQuartzJob {


    @Override
    public void execute(Object data) {
        OkFutureMarketService okFutureMarketService = ApplicationContextHolder.getContext().getBean(OkFutureMarketService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            okFutureMarketService.updateOkIndex(jobFuture.getSymbol());
        }
    }
}
