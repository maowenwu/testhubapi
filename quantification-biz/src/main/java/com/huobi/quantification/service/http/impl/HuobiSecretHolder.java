package com.huobi.quantification.service.http.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.common.api.HuobiSignature;
import com.huobi.quantification.entity.QuanAccountSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.service.account.HuobiAccountService;

/**
 * 将数据库配置的所有密钥对读取到map
 * 
 * @author lichenyang
 * @since 2018年7月17日
 */
@Component
public class HuobiSecretHolder {

	@Autowired
	private HuobiAccountService huobiAccountService;

	private Map<Long, List<QuanAccountSecret>> map = new ConcurrentHashMap<>();

	private Map<Long, Long> accountUsageCounter = new HashMap<>();

	@PostConstruct
	public void loadAllSecret() {
		List<Long> accountIds = huobiAccountService.findAccountByExchangeId(ExchangeEnum.HUOBI.getExId());
		if (accountIds.size() <= 0) {
			throw new RuntimeException("quan_account表未初始化账户数据");
		}
		for (Long accountId : accountIds) {
			List<QuanAccountSecret> secretList = huobiAccountService.findAccountSecretById(accountId);
			if (secretList.size() <= 0) {
				throw new RuntimeException("账户[" + accountId + "]，未配置对应的accessKey，secretKey");
			}
			map.put(accountId, secretList);
			accountUsageCounter.put(accountId, 0L);
		}
	}

	public synchronized HuobiSignature getHuobiSignatureById(Long accountId) {
		List<QuanAccountSecret> secrets = map.get(accountId);
		Long counter = accountUsageCounter.get(accountId);
		int index = (int) (counter % secrets.size());
		QuanAccountSecret secret = secrets.get(index);
		accountUsageCounter.put(accountId, counter + 1);
		HuobiSignature huobiSignature = new HuobiSignature(secret.getAccessKey(), secret.getSecretKey());
		return huobiSignature;
	}
}
