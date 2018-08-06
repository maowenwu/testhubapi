package com.huobi.quantification.strategy;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.api.common.ExchangeConfigService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ExchangeConfigResqDto;
import com.huobi.quantification.strategy.hedge.Hedger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {

	@Autowired
	Hedger hedger;


	@Autowired
	ExchangeConfigService exchangeConfigService;




	@Test
	public void getAllExchangeConfig() {
		ServiceResult<List<ExchangeConfigResqDto>> result = exchangeConfigService.getAllExchangeConfig();
		System.out.println("1=========" + JSON.toJSONString(result));
	}

}
