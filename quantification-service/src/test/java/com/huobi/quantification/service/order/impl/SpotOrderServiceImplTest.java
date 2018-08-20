package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.entity.StrategyHedgeConfig;
import com.huobi.quantification.enums.ExchangeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.SpotActiveOrderCancelReqDto;
import com.huobi.quantification.dto.SpotOrderCancelReqDto;
import com.huobi.quantification.dto.SpotOrderCancelReqDto.Orders;
import com.huobi.quantification.dto.SpotOrderExchangeReqDto;
import com.huobi.quantification.dto.SpotOrderInnerReqDto;
import com.huobi.quantification.dto.SpotOrderStatusReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.provider.SpotOrderServiceImpl;
import com.huobi.quantification.service.http.HttpService;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class SpotOrderServiceImplTest {
	@Autowired
	private SpotOrderServiceImpl spotOrderServiceImpl;

	@Autowired
	private HttpService httpService;

	@Autowired
	private QuanOrderMapper quanOrderMapper;

	@Autowired
	private StrategyHedgeConfigMapper strategyHedgeConfigMapper;

	@Test
	public void testUpdateOrderMapper() {
		List<Integer> arrayList = new ArrayList<>();
		arrayList.add(OrderStatusEnum.PRE_SUBMITTED.getOrderStatus());
		arrayList.add(OrderStatusEnum.SUBMITTED.getOrderStatus());
		List<QuanOrder> selectByOrderInfo = quanOrderMapper.selectByOrderInfo(arrayList);
		for (QuanOrder long1 : selectByOrderInfo) {
			System.err.println(long1.getOrderSourceId());
		}
	}






	@Test
	public void batchCancelOpenOrdershcancel() {
		Map<String, Object> param = new HashMap<>();
		param.put("account-id", 4295363l);
		param.put("symbol", "btcusdt");
		String body = httpService.doHuobiSpotPost(4295363l, HttpConstant.HUOBI_BATCHCANCELOPENORDERS, param);
		System.err.println("=============" + body);
	}


	@Test
	public void test1(){
        StrategyHedgeConfig strategyHedgeConfig = new StrategyHedgeConfig();
        List<StrategyHedgeConfig> strategyHedgeConfigs = strategyHedgeConfigMapper.selectList(strategyHedgeConfig);
        System.err.println(strategyHedgeConfigs.size());
    }
}
