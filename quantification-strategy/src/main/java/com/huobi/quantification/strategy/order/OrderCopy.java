package com.huobi.quantification.strategy.order;


import com.huobi.quantification.strategy.order.entity.DepthBook;
import com.huobi.quantification.strategy.order.entity.FutureOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
