package com.huobi.quantification.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.dto.SpotOrderCancelReqDto.Orders;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OrderStatusEnum;
import com.huobi.quantification.provider.SpotOrderServiceImpl;
import com.huobi.quantification.service.http.HttpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

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

	@Test
	public void test1(){
		QuanAccountFuture quanAccountFuture = new QuanAccountFuture();
		quanAccountFuture.setExchangeId(2);
		quanAccountFuture.setAccountSourceId(1L);
		List<QuanAccountFutureAsset> quanAccountFutureAssets = quanAccountFutureAssetMapper.selectByQuanAccountFuture(quanAccountFuture);
		Map<String, List<QuanAccountFutureAsset>> collect = quanAccountFutureAssets.stream().collect(Collectors.groupingBy(e -> e.getCoinType()));
		Map<String,QuanAccountFutureAsset> assetHashMap = new HashMap<>();
		collect.keySet().stream().forEach(e -> {
			List<QuanAccountFutureAsset> accountFutureAssets = collect.get(e);
			accountFutureAssets.stream().forEach(a -> {
				if (assetHashMap.get(e)==null){
					assetHashMap.put(a.getCoinType(),a);
				}else if (a.getCoinType().equals(assetHashMap.get(e).getCoinType())){
					int compareNum = assetHashMap.get(e).getUpdateTime().compareTo(a.getUpdateTime());
					if (compareNum < 0){
						assetHashMap.put(a.getCoinType(),a);
					}
				}
			});
		});
		List<QuanAccountFutureAsset> accountFutureAssets = new ArrayList<>();
		assetHashMap.keySet().stream().forEach(e -> accountFutureAssets.add(assetHashMap.get(e)));
		System.err.println(accountFutureAssets.size());
    }
}
