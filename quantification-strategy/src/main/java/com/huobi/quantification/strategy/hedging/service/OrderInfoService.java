package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;

@Component
public class OrderInfoService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotOrderService spotOrderService;

	/**
	 * 下单
	 * 
	 * @param priceMap          下单价格
	 * @param comparePosition   -1 卖 1买
	 * @param startHedgingParam
	 * @param position          寸头
	 * @return
	 */
	public ServiceResult<SpotPlaceOrderRespDto> placeHuobiSpotOrder(Map<String, BigDecimal> priceMap,
			StartHedgingParam startHedgingParam, BigDecimal position) {

		// -1, 0, or 1 判断买卖方向 --根据positionUSDT净头寸判断
		int comparePosition = position.compareTo(new BigDecimal(0));
		ServiceResult<SpotPlaceOrderRespDto> serviceResult = new ServiceResult<SpotPlaceOrderRespDto>();
		// 封装下单参数
		SpotPlaceOrderReqDto spotPlaceOrderReqDto = new SpotPlaceOrderReqDto();
		BigDecimal quantity = new BigDecimal(0.00);// 数量
		BigDecimal price = new BigDecimal(0.00); // 下单价格
		String side = "";// 买卖方向
		String orderType = "";// 买卖方式
		if (comparePosition == -1) {// USDT不够,需要下卖单，卖出对应的币种
			side = "sell";
			orderType = "limit";
			price = priceMap.get("sellPrice").multiply((new BigDecimal(1).add(startHedgingParam.getSlippage())));
			quantity = position.divide(price, 4, BigDecimal.ROUND_HALF_DOWN);
		} else if (comparePosition == 1) {// USDT足够,需要下买单，买入对应的币种
			side = "buy";
			orderType = "limit";
			price = priceMap.get("buyPrice").multiply((new BigDecimal(1).subtract(startHedgingParam.getSlippage())));
			quantity = position.divide(price, 8, BigDecimal.ROUND_HALF_DOWN).divide(
					(new BigDecimal(1).subtract(startHedgingParam.getFeeRate())), 4, BigDecimal.ROUND_HALF_DOWN);
		}

		spotPlaceOrderReqDto.setAccountId(startHedgingParam.getSpotAccountID());
		spotPlaceOrderReqDto.setExchangeId(startHedgingParam.getSpotExchangeId());
		spotPlaceOrderReqDto.setBaseCoin(startHedgingParam.getBaseCoin());
		spotPlaceOrderReqDto.setQuoteCoin(startHedgingParam.getQuoteCoin());
		spotPlaceOrderReqDto.setPrice(price.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_DOWN));
		spotPlaceOrderReqDto.setQuantity(quantity);
		spotPlaceOrderReqDto.setSide(side);
		spotPlaceOrderReqDto.setOrderType(orderType);
		logger.info("对冲下单请求参数为：  {} ", JSON.toJSONString(spotPlaceOrderReqDto));
		serviceResult = spotOrderService.placeOrder(spotPlaceOrderReqDto);
		return serviceResult;

	}

}
