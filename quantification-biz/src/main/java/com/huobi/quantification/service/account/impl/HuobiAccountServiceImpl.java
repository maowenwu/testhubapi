package com.huobi.quantification.service.account.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.enums.ExchangeEnum;
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
import com.huobi.quantification.dao.QuanAccountSecretMapper;
import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountSecret;
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
	private QuanAccountSecretMapper quanAccountSecretMapper;
	@Autowired
	private RedisService redisService;

	public Object accounts(String accountId) {
		Map<String, String> params = new HashMap<>();
		params.put("account-id", accountId);
		String body = httpService.doHuobiGet(Long.parseLong(accountId),
				HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId), params);
		parseAndSaveAccounts(body);
		return null;
	}

	private void parseAndSaveAccounts(String accountres) {
		JSONObject jsonObject = JSON.parseObject(accountres);
		String data = jsonObject.getString("data");
		JSONObject temp = JSON.parseObject(data);
//		QuanAccount quanAccount = new QuanAccount();
//		quanAccount.setAccountSourceId(temp.getLong("id"));
//		quanAccount.setAccountsType(temp.getString("type"));
//		quanAccount.setExchangeId(ExchangeEnum.HUOBI.getExId());
//		quanAccount.setState(temp.getString("state"));
		JSONArray jsarr = temp.getJSONArray("list");
//		quanAccountMapper.insert(quanAccount);
		List<QuanAccountAsset> assets = new ArrayList<>();
		for (int i = 0; i < jsarr.size(); i++) {
			if (i % 2 == 0) {
				QuanAccountAsset tempAccount = new QuanAccountAsset();
				tempAccount.setAccountId(temp.getLong("id"));
				JSONObject json1 = jsarr.getJSONObject(i);
				JSONObject json2 = jsarr.getJSONObject(i + 1);
				if ("trade".equals(json1.getString("type"))) {
					tempAccount.setAvailable(json1.getBigDecimal("balance"));
				}
				if ("frozen".equals(json2.getString("type"))) {
					tempAccount.setFrozen(json2.getBigDecimal("balance"));
				}
				if (null == tempAccount.getAvailable() || null == tempAccount.getFrozen()) {
					new RuntimeException("返回数据有误");
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
		redisService.saveHuobiAccountAsset(assets, temp.getLong("id"), ExchangeEnum.HUOBI.getExId());
	}

	@Override
	public void updateAccount() {
		logger.info("[HuobiUserInfo]任务开始");
		Stopwatch started = Stopwatch.createStarted();
		List<QuanAccount> accounts = quanAccountMapper.selectAll();
		for (QuanAccount quanAccount : accounts) {
			Long accountId = quanAccount.getAccountSourceId();
			Map<String, String> params = new HashMap<>();
			params.put("account-id", accountId + "");
			String body = httpService.doHuobiGet(accountId,
					HttpConstant.HUOBI_ACCOUNT.replaceAll("\\{account-id\\}", accountId + ""), params);
			parseAndSaveAccounts(body);
		}
		logger.info("[HuobiUserInfo]任务结束，耗时：" + started);
	}

	@Override
	public List<Long> findAccountByExchangeId(int exId) {
		List<Long> selectByExchangeId = quanAccountMapper.selectByExchangeId(exId);
		return selectByExchangeId;
	}

	@Override
	public List<QuanAccountSecret> findAccountSecretById(Long accountId) {
		List<QuanAccountSecret> secrets = quanAccountSecretMapper.selectByAccountId(accountId);
		return secrets;
	}
}
