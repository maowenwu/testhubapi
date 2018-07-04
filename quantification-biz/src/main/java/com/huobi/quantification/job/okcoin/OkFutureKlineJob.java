package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OkFutureKlineJob {

    @Autowired
    private MarketService marketService;


    public void execute() {
        marketService.storeOkFutureKline();
    }
}
