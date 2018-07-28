package com.huobi.quantification.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSON;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.dto.SpotKlineReqDto;
import com.huobi.quantification.dto.SpotKlineRespDto;
import com.huobi.quantification.enums.ExchangeEnum;

/**
 * 用于API现货接口功能调试
 * @author lichenyang
 * @since  2018年7月18日
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class SpotMarketServiceTest {
	
	@Autowired
	private SpotMarketService spotMarketService;
	
	@Test
	public void spotKlineTest() {
		SpotKlineReqDto spotKlineReqDto = new SpotKlineReqDto();
		spotKlineReqDto.setBaseCoin("btc");
		spotKlineReqDto.setQuoteCoin("usdt");
		spotKlineReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotKlineReqDto.setPeriod("5min");
		spotKlineReqDto.setIncludeNow(false);
		spotKlineReqDto.setSize(200);
		spotKlineReqDto.setTimeout(1000 * 20);
		spotKlineReqDto.setMaxDelay(1000 * 60 * 60);
		ServiceResult<SpotKlineRespDto> spotKline = spotMarketService.getKline(spotKlineReqDto);
		String jsonString = JSON.toJSONString(spotKline);
		System.err.println("jsonString:" + jsonString);
	}
	
	@Test
	public void SpotTradeTest() {
		SpotCurrentPriceReqDto currentPriceReqDto = new SpotCurrentPriceReqDto();
		currentPriceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		currentPriceReqDto.setBaseCoin("xrp");
		currentPriceReqDto.setQuoteCoin("btc");
		currentPriceReqDto.setMaxDelay(1000 * 60 * 60);
		currentPriceReqDto.setTimeout(1000 * 10);
		ServiceResult<SpotCurrentPriceRespDto> currentPrice = spotMarketService.getCurrentPrice(currentPriceReqDto);
		String jsonString = JSON.toJSONString(currentPrice);
		System.err.println("jsonString:" + jsonString);
	}
	
	@Test
	public void SpotDepthTest() {
		SpotDepthReqDto spotDepthReqDto = new SpotDepthReqDto();
		spotDepthReqDto.setBaseCoin("eth");
		spotDepthReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotDepthReqDto.setQuoteCoin("usdt");
		spotDepthReqDto.setMaxDelay(1000 * 60 * 60);
		spotDepthReqDto.setTimeout(1000 * 10);
		ServiceResult<SpotDepthRespDto> spotDepth = spotMarketService.getDepth(spotDepthReqDto);
		String jsonString = JSON.toJSONString(spotDepth);
		System.err.println("jsonString:" + jsonString);
	}
}
