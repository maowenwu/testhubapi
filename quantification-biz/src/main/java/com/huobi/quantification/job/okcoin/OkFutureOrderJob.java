package com.huobi.quantification.job.okcoin;

import com.huobi.quantification.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OkFutureOrderJob {

    @Autowired
    private OrderService orderService;


    public void execute() {
        orderService.storeOkFutureOrder();
    }
}
