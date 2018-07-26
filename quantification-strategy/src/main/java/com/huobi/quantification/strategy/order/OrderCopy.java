package com.huobi.quantification.strategy.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderCopy {

    @Autowired
    private OrderContext context;

    private DepthBookAdjuster depthBookAdjuster;

    @PostConstruct
    public void init() {
        depthBookAdjuster = new DepthBookAdjuster(context);
        context.init();
    }


    public void copyOrder() {


    }


}
