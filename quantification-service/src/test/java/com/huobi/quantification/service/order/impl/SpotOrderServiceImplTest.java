package com.huobi.quantification.service.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.huobi.quantification.enums.ExchangeEnum;
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

	@Test
	public void testUpdateOrderMapper() {
		List<Long> selectByOrderInfo = quanOrderMapper.selectByOrderInfo(1000L, OrderStatusEnum.FILLED.getOrderStatus(),
				"ethusdt");
		for (Long long1 : selectByOrderInfo) {
			System.err.println(long1);
		}
	}

	@Test
	public void getOrderByInnerOrderID() {
		SpotOrderInnerReqDto entity = new SpotOrderInnerReqDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		Long[] innerOrderID = { 35L, 36L };
		entity.setInnerOrderID(innerOrderID);
		spotOrderServiceImpl.getOrderByInnerOrderID(entity);
	}

	@Test
	public void getOrderByExOrderID() {
		SpotOrderExchangeReqDto entity = new SpotOrderExchangeReqDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		Long[] exOrderID = { 8010718329L, 36L };
		entity.setExOrderID(exOrderID);
		ServiceResult<Map<String, Object>> result = spotOrderServiceImpl.getOrderByExOrderID(entity);
		System.out.println("=====code:" + result.getCode());
		System.out.println("=====size:" + result.getData().size());
		System.out.println("====result:" + JSON.toJSONString(result));
	}

	@Test
	public void cancelActiveOrder() {
		SpotActiveOrderCancelReqDto reqDto = new SpotActiveOrderCancelReqDto();
		reqDto.setExchangeID(1);
		reqDto.setAccountID(4232061l);
		reqDto.setParallel(false);
		reqDto.setBaseCoin("eos");
		reqDto.setQuoteCoin("btc");
		spotOrderServiceImpl.cancelOrder(reqDto);
	}

	/**
	 * 撤销订单-根据内部orderID
	 */
	@Test
	public void cancelOrder() {
		SpotOrderCancelReqDto reqDto = new SpotOrderCancelReqDto();
		List<Orders> list = new ArrayList<>();
		Orders order1 = new Orders();
		Orders order2 = new Orders();
		order1.setInnerOrderID(2l);
		order2.setInnerOrderID(3l);
		list.add(order1);
		list.add(order2);
		reqDto.setExchangeID(1);
		reqDto.setAccountID(4295363l);
		reqDto.setOrders(list);
		reqDto.setParallel(false);
		spotOrderServiceImpl.cancelOrder(reqDto);
	}

	@Test
	public void getOrderByStatus() {
		SpotOrderStatusReqDto entity = new SpotOrderStatusReqDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		entity.setStatus("submitted");
		spotOrderServiceImpl.getOrderByStatus(entity);
	}

	@Test
	public void batchCancelOpenOrdershcancel() {
		Map<String, Object> param = new HashMap<>();
		param.put("account-id", 4295363l);
		param.put("symbol", "btcusdt");
		String body = httpService.doHuobiPost(4295363l, HttpConstant.HUOBI_BATCHCANCELOPENORDERS, param);
		System.err.println("=============" + body);
	}

	@Test
	public void orderPlace() {
		SpotPlaceOrderReqDto reqDto = new SpotPlaceOrderReqDto();
		reqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		reqDto.setAccountId(4295363);
		reqDto.setBaseCoin("btc");
		reqDto.setQuoteCoin("usdt");
		reqDto.setSide("buy");
		reqDto.setOrderType("limit");
		reqDto.setPrice(new BigDecimal("0.01"));
		reqDto.setQuantity(new BigDecimal("1"));
		reqDto.setLinkOrderId(123);
		reqDto.setSync(true);
		ServiceResult<SpotPlaceOrderRespDto> placeOrder = spotOrderServiceImpl.placeOrder(reqDto);
		System.err.println(JSON.toJSONString(placeOrder));
	}
}
