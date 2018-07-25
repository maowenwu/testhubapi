package com.huobi.quantification.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;

/**
 * 用于SpotAccountService接口的测试
 * @author lichenyang
 * @since  2018年7月18日
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class SpotAccountServiceTest {
	
	@Autowired
	private SpotAccountService spotAccountService;
	
	@Test
	public void getBalanceTest() {
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(4232061L);
		spotBalanceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotBalanceReqDto.setMaxDelay(1000 * 60 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(spotBalanceReqDto);
		String jsonString = JSON.toJSONString(balance);
		System.err.println(jsonString);
	}
}
