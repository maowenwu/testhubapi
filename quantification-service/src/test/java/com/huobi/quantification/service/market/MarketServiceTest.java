package com.huobi.quantification.service.market;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.enums.OkContractType;
import com.huobi.quantification.enums.OkSymbolEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class MarketServiceTest {

    @Autowired
    private MarketService marketService;

    @Test
    public void getOkTicker() {
        Object okTicker = marketService.getOkTicker("btc_usd", "this_week");
        System.out.println(okTicker);
    }


    @Test
    public void getOkDepth() {
        Object okTicker = marketService.getOkDepth("btc_usd", "this_week");
        System.out.println(okTicker);
    }

    @Test
    public void getOkFutureKline() {
        Object kline = marketService.getOkFutureKline("btc_usd", "1min", "this_week", 2, 1530265560000L);
        System.out.println(kline);
    }

    @Test
    public void getLatestOkFutureKline() {
        marketService.getLatestOkFutureKline(OkSymbolEnum.BCH_USD.getSymbol(), "1min", OkContractType.NEXT_WEEK.getType());
        System.out.println();
    }


}