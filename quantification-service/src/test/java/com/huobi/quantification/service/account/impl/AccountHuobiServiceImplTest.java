package com.huobi.quantification.service.account.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.huobi.quantification.enums.ExchangeEnum;
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
		Map<String, String> params = new HashMap<>();
		params.put("account-id", "4232061");
		String doHuobiGet = httpService.doHuobiGet(4232061L,HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", "4232061"),
				params);
		JSONObject jsonObject = JSON.parseObject(doHuobiGet);
		String data = jsonObject.getString("data");
		JSONObject temp = JSON.parseObject(data);
		QuanAccount quanAccount = new QuanAccount();
		quanAccount.setAccountSourceId(temp.getLong("id"));
		quanAccount.setAccountsType(temp.getString("type"));
		quanAccount.setExchangeId(ExchangeEnum.HUOBI.getExId());
		quanAccount.setState(temp.getString("state"));
		JSONArray jsarr = temp.getJSONArray("list");
		quanAccountMapper.insert(quanAccount);
		List<QuanAccountAsset> assets = new ArrayList<>();
		for (int i = 0; i < jsarr.size(); i++) {
			if (i % 2 == 0 ) {
				QuanAccountAsset tempAccount = new QuanAccountAsset();
				tempAccount.setAccountId(quanAccount.getAccountSourceId());
				JSONObject json1 = jsarr.getJSONObject(i);
				JSONObject json2 = jsarr.getJSONObject(i+1);
				if (json1.getString("type").equals("trade")) {
					tempAccount.setAvailable(json1.getBigDecimal("balance"));
				}else {
					tempAccount.setFrozen(json1.getBigDecimal("balance"));
				}
				if (json2.getString("type").equals("frozen")) {
					tempAccount.setFrozen(json2.getBigDecimal("balance"));
				}else {
					tempAccount.setAvailable(json2.getBigDecimal("balance"));
				}
				tempAccount.setCoin(json1.getString("currency"));
				tempAccount.setDataUpdate(new Date());
				tempAccount.setTs(new Date());
				tempAccount.setTotal(tempAccount.getAvailable().add(tempAccount.getFrozen()));
				quanAccountAssetMapper.insert(tempAccount);
				assets.add(tempAccount);
			}
			continue;
		}
		redisService.saveHuobiAccountAsset(assets, quanAccount.getAccountSourceId(), quanAccount.getExchangeId());
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
