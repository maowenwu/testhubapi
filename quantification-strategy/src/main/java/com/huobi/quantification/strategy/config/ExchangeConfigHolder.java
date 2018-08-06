package com.huobi.quantification.strategy.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.dao.QuanExchangeConfigMapper;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.xiaoleilu.hutool.util.CollectionUtil;

@Component
public class ExchangeConfigHolder {

	@Autowired
	private QuanExchangeConfigMapper quanExchangeConfigMapper;

	private Map<String, QuanExchangeConfig> map = new ConcurrentHashMap<>();

	@PostConstruct
	public void loadAllExchangeConfig() {
		List<QuanExchangeConfig> list = quanExchangeConfigMapper.selectAll();
		if (CollectionUtil.isEmpty(list)) {
			throw new RuntimeException("quan_exchange_config表未初始化账户数据");
		}
		for (QuanExchangeConfig temp : list) {
			map.put(temp.getBaseCoin() + temp.getQuoteCoin(), temp);
		}
	}

	public synchronized QuanExchangeConfig getExchangeConfigBySymbol(String symbol) {
		QuanExchangeConfig quanExchangeConfig = map.get(symbol);
		return quanExchangeConfig;
	}

}
