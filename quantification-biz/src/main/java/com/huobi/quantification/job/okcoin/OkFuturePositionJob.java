package com.huobi.quantification.job.okcoin;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.huobi.quantification.service.account.AccountService;
import com.huobi.quantification.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OkFuturePositionJob implements SimpleJob {

    @Autowired
    private AccountService accountService;

    @Override
    public void execute(ShardingContext shardingContext) {
        accountService.storeAllOkPosition();
    }
}
