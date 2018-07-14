package com.huobi.quantification.service.order.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.provider.SpotOrderServiceImpl;

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

}
