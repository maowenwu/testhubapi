package com.huobi.quantification.strategy.hedge.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.enums.ExchangeEnum;

@Component
public class MarketInfoService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotMarketService spotMarketService;

	/**
	 * 获取买一卖一价格
	 * 
	 * @param spotDepthReqDto
	 * @return
	 */
	public Map<String, BigDecimal> getHuoBiSpotBuyOneSellOnePrice(SpotDepthReqDto spotDepthReqDto) {
		spotDepthReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotDepthReqDto.setMaxDelay(10000L);
		spotDepthReqDto.setTimeout(10000L);
		logger.info("获取火币现货买一卖一价格请求参数为 {}     ", JSON.toJSONString(spotDepthReqDto));
		ServiceResult<SpotDepthRespDto> serviceResult = spotMarketService.getDepth(spotDepthReqDto);
		BigDecimal buyPrice = serviceResult.getData().getData().getAsks().get(0).getPrice();// 买盘,[price(成交价),
																							// amount(成交量)], 按price降序,
		BigDecimal sellPrice = serviceResult.getData().getData().getBids().get(0).getPrice();// 卖盘,[price(成交价),
																								// amount(成交量)],
																								// 按price升序
		Map<String, BigDecimal> resultMap = new HashMap<>();
		resultMap.put("buyPrice", buyPrice);
		resultMap.put("sellPrice", sellPrice);
		logger.info("获取火币现货买一卖一价格的结果为 {}     ", JSON.toJSONString(resultMap));
		return resultMap;
	}
}
