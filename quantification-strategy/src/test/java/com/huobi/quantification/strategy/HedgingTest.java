package com.huobi.quantification.strategy;

import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.api.comm.ExchangeConfigService;
import com.huobi.quantification.strategy.hedge.Hedger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {

	@Autowired
	Hedger hedger;


	@Autowired
	ExchangeConfigService exchangeConfigService;



}
