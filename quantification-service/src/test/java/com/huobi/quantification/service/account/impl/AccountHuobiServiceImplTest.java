package com.huobi.quantification.service.account.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.enums.ExchangeEnum;
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
	
	@Test
    public void updateAccount(){
		String str="{\r\n" + 
				"  \"status\": \"ok\",\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"id\": 100009,\r\n" + 
				"    \"type\": \"spot\",\r\n" + 
				"    \"state\": \"working\",\r\n" + 
				"    \"list\": [\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"usdt\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"500009195917.4362872650\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"usdt\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"328048.1199920000\"\r\n" + 
				"      },\r\n" + 
				"     {\r\n" + 
				"        \"currency\": \"etc\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"etc\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      },\r\n" + 
				"     {\r\n" + 
				"        \"currency\": \"eth\",\r\n" + 
				"        \"type\": \"trade\",\r\n" + 
				"        \"balance\": \"499999894616.1302471000\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"currency\": \"eth\",\r\n" + 
				"        \"type\": \"frozen\",\r\n" + 
				"        \"balance\": \"9786.6783000000\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"user-id\": 1000\r\n" + 
				"  }\r\n" + 
				"}";
		JSONObject jsonObject = JSON.parseObject(str);
		String data = jsonObject.getString("data");
		JSONObject temp = JSON.parseObject(data);
		QuanAccount quanAccount = new QuanAccount();
		quanAccount.setAccountSourceId(temp.getLong("id"));
		quanAccount.setAccountsType(temp.getString("type"));
		quanAccount.setExchangeId(ExchangeEnum.HUOBI.getExId());
		quanAccount.setState(temp.getString("state"));
		JSONArray jsarr = temp.getJSONArray("list");
		quanAccountMapper.insert(quanAccount);
		redisService.saveHuobiAccount(quanAccount);
		QuanAccountAsset quanAccountAsset = new QuanAccountAsset();
		QuanAccountAsset tempAccount = new QuanAccountAsset();
		for (int i = 0; i < jsarr.size(); i++) {
			JSONObject list = jsarr.getJSONObject(i);
			if (i > 0 && tempAccount.getCoin().equals(list.getString("currency"))) {
				if (list.getString("type").equals("frozen")) {
					quanAccountAsset.setFrozen(new BigDecimal(list.getString("balance")));
					quanAccountAssetMapper.insert(quanAccountAsset);
					redisService.saveHuobiAccountAsset(quanAccountAsset,quanAccount.getAccountSourceId());
				}
				continue;
			}
			quanAccountAsset.setAccountId(quanAccount.getAccountSourceId());
			quanAccountAsset.setCoin(list.getString("currency"));
			quanAccountAsset.setDataUpdate(new Date());
			if (list.getString("type").equals("trade")) {
				quanAccountAsset.setAvailable(new BigDecimal(list.getString("balance")));
			}
			tempAccount = quanAccountAsset;
		}
	}
	
}
