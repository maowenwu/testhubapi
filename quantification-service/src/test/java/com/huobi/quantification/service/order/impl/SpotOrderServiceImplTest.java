package com.huobi.quantification.service.order.impl;

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
import com.huobi.quantification.dto.SpotOrderReqCancelDto;
import com.huobi.quantification.dto.SpotOrderReqExchangeDto;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderReqStatusDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.provider.SpotOrderServiceImpl;
import com.huobi.quantification.service.http.HttpService;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;

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
		List<Long> selectByOrderInfo = quanOrderMapper.selectByOrderInfo(1000L, "filled", "ethusdt");
		for (Long long1 : selectByOrderInfo) {
			System.err.println(long1);
		}
	}

	@Test
	public void getOrderByInnerOrderID() {
		SpotOrderReqInnerDto entity = new SpotOrderReqInnerDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		Long[] innerOrderID = { 35L, 36L };
		entity.setInnerOrderID(innerOrderID);
		ServiceResult<Map<String, Object>> result = spotOrderServiceImpl.getOrderByInnerOrderID(entity);
		System.out.println("=====code:" + result.getCode());
		System.out.println("=====size:" + result.getData().size());
		System.out.println("====result:" + JSON.toJSONString(result));
	}

	@Test
	public void getOrderByStatus() {
		SpotOrderReqStatusDto entity = new SpotOrderReqStatusDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		entity.setStatus("submitted");
		ServiceResult<List<SpotOrderRespDto>> result = spotOrderServiceImpl.getOrderByStatus(entity);
		System.out.println("=====code:" + result.getCode());
		System.out.println("=====size:" + result.getData().size());
		System.out.println("====result:" + JSON.toJSONString(result));
	}

	@Test
	public void getOrderByExOrderID() {
		SpotOrderReqExchangeDto entity = new SpotOrderReqExchangeDto();
		entity.setExchangeID(0);
		entity.setAccountID(4295363l);
		Long[] exOrderID = { 8010718329L, 36L };
		entity.setExOrderID(exOrderID);
		ServiceResult<Map<String, Object>> result = spotOrderServiceImpl.getOrderByExOrderID(entity);
		System.out.println("=====code:" + result.getCode());
		System.out.println("=====size:" + result.getData().size());
		System.out.println("====result:" + JSON.toJSONString(result));
	}

	/**
	 * 撤销订单-根据内部orderID
	 */
	@Test
	public void cancelOrder() {
		System.out.println("1==============");
		String param = "{\r\n" + "    \"exchangeID\":0,\r\n" + "    \"accountID\":4295363,\r\n" + "    \"orders\":[\r\n"
				+ "        {\r\n" + "            \"innerOrderID\":8,\r\n" + "            \"exOrderID\":null,\r\n"
				+ "            \"linkOrderID\":\"\",\r\n" + "            \"baseCoin\":444,\r\n"
				+ "            \"QuoteCoin\":5555\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"innerOrderID\":null,\r\n" + "            \"exOrderID\":7945903430,\r\n"
				+ "            \"linkOrderID\":333,\r\n" + "            \"baseCoin\":444,\r\n"
				+ "            \"QuoteCoin\":5555\r\n" + "        }\r\n" + "    ]\r\n" + "}";
		// 1、使用JSONObject
		JSONObject json = JSONUtil.parseObj(param);
		SpotOrderReqCancelDto result = JSONUtil.toBean(json, SpotOrderReqCancelDto.class);
		ServiceResult<Map<String, Object>> resultMap = spotOrderServiceImpl.cancelOrder(result);
		System.out.println("==========resultMap:" + JSONUtil.toJsonStr(resultMap));
		System.out.println("2==============");
	}

	/**
	 * 撤销订单-根据内部orderID
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void batchcancel() throws InterruptedException {
		System.out.println("1==============");
		String param = "{\"status\":\"ok\",\"data\":{\"success\":[],\"failed\":[{\"err-msg\":\"the order state is error\",\"order-id\":\"7996038802\",\"err-code\":\"order-orderstate-error\"},{\"err-msg\":\"the order state is error\",\"order-id\":\"7996039844\",\"err-code\":\"order-orderstate-error\"},{\"err-msg\":\"the order state is error\",\"order-id\":\"7996040440\",\"err-code\":\"order-orderstate-error\"}]}}";
		// 1、使用JSONObject
		JSONObject json = JSONUtil.parseObj(param);
		SpotOrderReqCancelDto result = JSONUtil.toBean(json, SpotOrderReqCancelDto.class);
		ServiceResult<Map<String, Object>> resultMap = spotOrderServiceImpl.cancelOrder(result);
		System.out.println("==========resultMap:" + JSONUtil.toJsonStr(resultMap));
		System.out.println("2==============");
	}

	@Test
	public void batchCancelOpenOrdershcancel() {
		Map<String, Object> param = new HashMap<>();
		param.put("account-id", 4295363l);
		param.put("symbol", "btcusdt");
		String body = httpService.doHuobiPost(4295363l, HttpConstant.HUOBI_BATCHCANCELOPENORDERS, param);
		System.err.println("=============" + body);
	}

}
