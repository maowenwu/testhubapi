package com.huobi.quantification.service.order.impl;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.request.future.FutureHuobiOrderRequest;
import com.huobi.quantification.service.order.HuobiFutureOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class HuobiFutureOrderServiceImplTest {

	@Autowired
	private HuobiFutureOrderService huobiFutureOrderService;

	@Test
	public void placeOrder() {
		FutureHuobiOrderRequest request = new FutureHuobiOrderRequest();
		request.setSymbol("BTC");
		request.setContractType("next_week");
		request.setPrice("200");
		request.setVolume("10");
		request.setDirection("buy");
		request.setOffset("open");
		request.setLeverRate("10");
		request.setOrderPriceType("limit");
		request.setClientOrderId(System.currentTimeMillis() + "");
		Long aLong = huobiFutureOrderService.placeOrder(request);
		System.out.println(aLong);
	}

	@Test
	public void cancelOrder() {
		Long aLong = huobiFutureOrderService.cancelOrder(8L, 8L);
		System.out.println(aLong);
	}

	@Test
	public void replenishOrder() {
		huobiFutureOrderService.replenishOrder(156138L,"ETH");
	}
}
