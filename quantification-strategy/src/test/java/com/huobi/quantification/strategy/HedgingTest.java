package com.huobi.quantification.strategy;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.strategy.hedging.StartHedging;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {

	@Autowired
	StartHedging startHedging;
	
	@Autowired
	QuanAccountHistoryMapper quanAccountHistoryMapper;

	@Test
	public void start() throws Exception {
		StartHedgingParam startHedgingParam = new StartHedgingParam();
		startHedgingParam.setBaseCoin("btc");
		startHedgingParam.setFeeRate(new BigDecimal(0));
		startHedgingParam.setQuoteCoin("usdt");
		startHedgingParam.setSlippage(new BigDecimal(0));
		startHedgingParam.setSpotAccountID(4295363L);
		startHedgingParam.setSpotExchangeId(1);
		startHedgingParam.setFutureAccountID(0L);
		startHedgingParam.setFutureExchangeId(0);
		while (true) {
			startHedging.startNormal(startHedgingParam);
			Thread.sleep(1000 * 60);
		}
	}

	@Test
	public void getAccountHistory() {
		BigDecimal result = quanAccountHistoryMapper.getInitAmount(111L, 1,  "btcusdt");
		System.out.println("1=========" + result);
	}

}
