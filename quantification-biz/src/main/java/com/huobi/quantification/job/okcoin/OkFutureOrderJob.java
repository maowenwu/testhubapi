package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.MarketService;
import com.huobi.quantification.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class OkFutureOrderJob extends AbstractQuartzJob {

    @Override
    public void execute(Object data) {
        OrderService orderService = ApplicationContextHolder.getContext().getBean(OrderService.class);
        if (data instanceof QuanJobFuture) {
            QuanJobFuture jobFuture = (QuanJobFuture) data;
            orderService.updateOkOrderInfo(jobFuture.getAccountId(), jobFuture.getSymbol(), jobFuture.getContractType());
        }
    }
}
