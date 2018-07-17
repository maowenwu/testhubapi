package com.huobi.quantification.service.account.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
		QuanAccountAsset quanAccountAsset = new QuanAccountAsset();
		QuanAccountAsset tempAccount = new QuanAccountAsset();
		for (int i = 0; i < jsarr.size(); i++) {
			JSONObject list = jsarr.getJSONObject(i);
			if (i > 0 && tempAccount.getCoin().equals(list.getString("currency"))) {
				if (list.getString("type").equals("frozen")) {
					quanAccountAsset.setFrozen(new BigDecimal(list.getString("balance")));
					quanAccountAsset.setTotal(quanAccountAsset.getFrozen().add(quanAccountAsset.getAvailable()));
					quanAccountAssetMapper.insert(quanAccountAsset);
				}
				continue;
			}
			quanAccountAsset.setAccountId(quanAccount.getAccountSourceId());
			quanAccountAsset.setCoin(list.getString("currency"));
			quanAccountAsset.setDataUpdate(new Date());
			quanAccountAsset.setTs(new Date());
			if (list.getString("type").equals("trade")) {
				quanAccountAsset.setAvailable(new BigDecimal(list.getString("balance")));
			}
			tempAccount = quanAccountAsset;
		}
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
