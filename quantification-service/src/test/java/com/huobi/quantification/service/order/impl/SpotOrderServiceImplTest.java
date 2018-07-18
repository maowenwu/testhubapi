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
import com.huobi.quantification.dto.SpotOrderReqCancelDto;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.provider.SpotOrderServiceImpl;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class SpotOrderServiceImplTest {
	@Autowired
	private SpotOrderServiceImpl spotOrderServiceImpl;

	@Test
	public void getOrderByInnerOrderID() {
		SpotOrderReqInnerDto entity = new SpotOrderReqInnerDto();
		ServiceResult<List<SpotOrderRespDto>> result = spotOrderServiceImpl.getOrderByInnerOrderID(entity);
		System.out.println("=====code:" + result.getCode());
		System.out.println("=====list:" + result.getData());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {

		System.out.println("1==============");
		Map<String, Object> resultMap = new HashMap<>();
		String param = "{\"status\":\"ok\",\"data\":{\"success\":[],\"failed\":[{\"err-msg\":\"the order state is error\",\"order-id\":\"7996038802\",\"err-code\":\"order-orderstate-error\"},{\"err-msg\":\"the order state is error\",\"order-id\":\"7996039844\",\"err-code\":\"order-orderstate-error\"},{\"err-msg\":\"the order state is error\",\"order-id\":\"7996040440\",\"err-code\":\"order-orderstate-error\"}]}}";
		Map maps = (Map)JSON.parse(param);
		Object object=maps.get("data");
		Map<String, Object> objectMap = (Map<String, Object>)JSON.parse(object.toString());
		List successMap = (List)JSON.parse(objectMap.get("success").toString());
		List<Map> failMap =(List<Map>)JSON.parse(objectMap.get("failed").toString());
		for(Map tempMap:failMap) {
			tempMap.put("innerOrderID",tempMap.get("order-id"));
			tempMap.put("error_code",tempMap.get("err-code"));
			tempMap.remove("order-id");
			tempMap.remove("err-code");
			tempMap.remove("err-msg");
		}
		resultMap.put("success", successMap);
		resultMap.put("fail", failMap);
		System.out.println("=========="+successMap);
		System.out.println("=========="+failMap);
		System.out.println("=========="+resultMap);

	}

}
