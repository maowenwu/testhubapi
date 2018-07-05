package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.MarketService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
public class OkFutureKlineJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        MarketService marketService = ApplicationContextHolder.getContext().getBean(MarketService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            marketService.updateOkFutureKline(jobFuture.getSymbol(), "1min", jobFuture.getContractType());
        }
    }
}
