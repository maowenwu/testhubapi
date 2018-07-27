package com.huobi.quantification.strategy.hedging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;

@Component
public class OrderUtil {
	
	@Autowired
	SpotOrderService spotOrderService;
	
	/**
	 * 下单
	 * @param reqDto
	 */
	public void  placeHuobiSpotOrder(SpotPlaceOrderReqDto reqDto){
		ServiceResult<SpotPlaceOrderRespDto> serviceResult=spotOrderService.placeOrder(reqDto);
	}

}
