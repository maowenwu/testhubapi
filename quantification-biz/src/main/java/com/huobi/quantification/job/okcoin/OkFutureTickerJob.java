package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class OkFutureTickerJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        MarketService marketService = ApplicationContextHolder.getContext().getBean(MarketService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            marketService.updateOkTicker(jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
