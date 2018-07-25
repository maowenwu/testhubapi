package com.huobi.quantification.strategy.order.client;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotDepthRespDto;
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
        ServiceResult<SpotDepthRespDto> btc_usd = huobiSpotMarketClient.getDepth("btc_usd");
        System.out.println(JSON.toJSONString(btc_usd));
    }
}
