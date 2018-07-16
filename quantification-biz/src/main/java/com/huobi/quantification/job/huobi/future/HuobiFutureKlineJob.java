package com.huobi.quantification.job.huobi.future;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.HuobiFutureMarketService;
import com.huobi.quantification.service.market.OkFutureMarketService;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class HuobiFutureKlineJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        HuobiFutureMarketService futureMarketService = ApplicationContextHolder.getContext().getBean(HuobiFutureMarketService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            futureMarketService.updateHuobiKline(jobFuture.getSymbol(), jobFuture.getContractType(), "1min");
        }
    }
}
