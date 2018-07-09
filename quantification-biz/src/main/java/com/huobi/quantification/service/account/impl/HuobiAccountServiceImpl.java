package com.huobi.quantification.service.account.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.service.account.HuobiAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
@DependsOn("httpServiceImpl")
@Service
@Transactional
public class HuobiAccountServiceImpl implements HuobiAccountService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpService httpService;
	@Autowired
	private QuanAccountMapper quanAccountMapper;
	@Autowired
	private QuanAccountAssetMapper quanAccountAssetMapper;
	@Autowired
	private RedisService redisService;

	public Object accounts(String accountId) {
		Map<String, String> params = new HashMap<>();
		String body = httpService.doGet(HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId), params);
		parseAndaccounts(body);
		return null;
	}

	private void parseAndaccounts(String accountres) {
		JSONObject jsonObject = JSON.parseObject(accountres);
		String data = jsonObject.getString("data");
		JSONObject temp = JSON.parseObject(data);
		QuanAccount quanAccount = new QuanAccount();
		quanAccount.setAccountSourceId(temp.getLong("user-id"));
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

	@Override
	public void updateAccount(String accountId) {
		Stopwatch started = Stopwatch.createStarted();
		logger.info("[HuobiUserInfo][accountId={}]任务开始", accountId);
		Map<String, String> params = new HashMap<>();
		String body = httpService.doGet(HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId), params);
		parseAndaccounts(body);
		logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountId);
	}
}
