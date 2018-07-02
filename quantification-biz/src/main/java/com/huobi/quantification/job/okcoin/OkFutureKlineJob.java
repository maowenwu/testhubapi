package com.huobi.quantification.job.okcoin;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OkFutureKlineJob implements SimpleJob {

    @Autowired
    private MarketService marketService;

    @Override
    public void execute(ShardingContext shardingContext) {
        marketService.storeOkFutureKline();
    }
}
