package com.huobi.quantification.strategy.entity;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.strategy.order.OrderContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class OrderContextTest {


    @Autowired
    private OrderContext orderContext;

    @Test
    public void testGetDepth() {
        DepthBook eth_usdt = orderContext.getDepth();
        List<DepthBook.Depth> asks = eth_usdt.getAsks();

        System.out.println(JSON.toJSONString(eth_usdt));
    }

    @Test
    public void placeOrder() {
        orderContext.placeOrder(1,1,BigDecimal.valueOf(152.24),BigDecimal.valueOf(10));
    }

    @Test
    public void cancelAllOrder(){
        orderContext.cancelAllOrder();
    }

    @Test
    public void init() {
    }

    @Test
    public void getDepth() {
    }

    @Test
    public void getExchangeRateOfUSDT2USD() {
    }

    @Test
    public void getSpotBalance() {
    }

    @Test
    public void getFutureBalance() {
    }


    @Test
    public void getStrategyOrderConfig() {
    }

    @Test
    public void getActiveOrderMap() {
        orderContext.getActiveOrderMap();
    }

    @Test
    public void cancelOrderNotInDepthBook() {
    }

    @Test
    public void placeBuyOrder() {
    }

    @Test
    public void cancelOrder() {
    }

    @Test
    public void placeSellOrder() {
    }

    @Test
    public void placeSellOpenOrder() {
    }

    @Test
    public void placeSellCloseOrder() {
    }

    @Test
    public void setOrderReader() {
    }

    @Test
    public void setPosition() {
    }

    @Test
    public void setConfig() {
    }

    @Test
    public void setFutureBalance() {
    }

    @Test
    public void setSpotBalance() {
    }
}
