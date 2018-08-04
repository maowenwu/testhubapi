package com.huobi.quantification.strategy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.api.common.ExchangeConfigService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dto.ExchangeConfigResqDto;
import com.huobi.quantification.strategy.hedging.entity.StartHedgingParam;
import com.huobi.quantification.strategy.hedging.service.StartHedgingService;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {

	@Autowired
	StartHedgingService startHedgingService;

	@Autowired
	QuanAccountHistoryMapper quanAccountHistoryMapper;
	@Autowired
	ExchangeConfigService exchangeConfigService;

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
			startHedgingService.startNormal(startHedgingParam);
			Thread.sleep(1000 * 60);
		}
	}

	@Test
	public void getAccountHistory() {
		BigDecimal result = quanAccountHistoryMapper.getInitAmount(111L, 1, "btcusdt");
		System.out.println("1=========" + result);
	}

	@Test
	public void getAllExchangeConfig() {
		ServiceResult<List<ExchangeConfigResqDto>> result = exchangeConfigService.getAllExchangeConfig();
		System.out.println("1=========" + JSON.toJSONString(result));
	}

}
