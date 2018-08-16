package com.huobi.quantification.service.account.impl;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class AccountHuobiServiceImplTest {

	@Autowired
	private QuanAccountMapper quanAccountMapper;

	@Autowired
	private QuanAccountAssetMapper quanAccountAssetMapper;

	@Autowired
	private RedisService redisService;

	@Autowired
	private HttpService httpService;

	@Test
	public void updateAccount() {

	}

	/**
	 * 查询当前用户的所有账户(即account-id)，Pro站和HADAX account-id通用
	 */
	@Test
    public void accounts (){
		String result ="";
		result=httpService.doHuobiSpotGet(4232061L,"https://api.huobipro.com"+"/v1/account/accounts",new HashMap<>());
		System.out.println("========="+result);
	}
	
}
