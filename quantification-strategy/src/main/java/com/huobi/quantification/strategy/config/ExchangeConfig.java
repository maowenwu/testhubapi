package com.huobi.quantification.strategy.config;


import com.huobi.quantification.dao.QuanExchangeConfigMapper;
import com.huobi.quantification.entity.QuanExchangeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExchangeConfig {

    @Autowired
    private QuanExchangeConfigMapper quanExchangeConfigMapper;

    private static Map<String, QuanExchangeConfig> configMap = new HashMap<>();

    @PostConstruct
    public void loadConfig() {
        List<QuanExchangeConfig> exchangeConfigs = quanExchangeConfigMapper.selectAll();
        exchangeConfigs.forEach(e -> {
            configMap.put(e.getExchangeId() + e.getBaseCoin() + e.getQuoteCoin(), e);
        });
    }

    public static QuanExchangeConfig getExchangeConfig(int exchangeId, String baseCoin, String quotCoin) {
        return configMap.get(exchangeId + baseCoin + quotCoin);
    }

}
