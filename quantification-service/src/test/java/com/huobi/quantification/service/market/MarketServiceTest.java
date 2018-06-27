package com.huobi.quantification.service.market;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanAccountsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class MarketServiceTest {

    @Autowired
    private MarketService marketService;

    @Test
    public void getOkTicker(){
        Object okTicker = marketService.getOkTicker("btc_usd", "this_week");
        System.out.println(okTicker);
    }
}