package com.huobi.quantification.service.account.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.enums.ExchangeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
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
		result=httpService.doHuobiGet(4232061L,"https://api.huobipro.com"+"/v1/account/accounts",new HashMap<>());
		System.out.println("========="+result);
	}
	
}
