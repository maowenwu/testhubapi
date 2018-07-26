package com.huobi.quantification.strategy.order.entity;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.strategy.order.OrderContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class OrderContextTest {


    @Autowired
    private OrderContext orderContext;

    @Test
    public void testGetDepth() {
        DepthBook eth_usdt = orderContext.getDepth("btc_usdt");
        List<DepthBook.Depth> asks = eth_usdt.getAsks();

        System.out.println(JSON.toJSONString(eth_usdt));
    }
}
