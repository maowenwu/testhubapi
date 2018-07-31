package com.huobi.quantification.strategy;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.strategy.hedging.StartHedging;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {

	@Autowired
	StartHedging startHedging;

	@Test
	public void start() {
		StartHedgingParam startHedgingParam = new StartHedgingParam();
		startHedgingParam.setBaseCoin("btc");
		startHedgingParam.setFeeRate(new BigDecimal(0));
		startHedgingParam.setQuoteCoin("usdt");
		startHedgingParam.setSlippage(new BigDecimal(0));
		startHedgingParam.setSpotAccountID(4295363L);
		startHedgingParam.setSpotExchangeId(1);
		startHedging.startNormal(startHedgingParam);
	}

}
