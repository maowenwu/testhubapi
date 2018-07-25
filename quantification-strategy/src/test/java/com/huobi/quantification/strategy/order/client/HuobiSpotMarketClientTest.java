package com.huobi.quantification.strategy.order.client;


import com.huobi.quantification.StrategyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HuobiSpotMarketClientTest {


    @Autowired
    private HuobiSpotMarketClient huobiSpotMarketClient;

    @Test
    public void testGetDepth() {
        huobiSpotMarketClient.getDepth("btc_usd");
    }
}
