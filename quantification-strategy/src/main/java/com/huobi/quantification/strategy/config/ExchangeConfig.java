package com.huobi.quantification.strategy.config;


import com.huobi.quantification.dao.QuanExchangeConfigMapper;
import com.huobi.quantification.entity.QuanExchangeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExchangeConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuanExchangeConfigMapper quanExchangeConfigMapper;

    private static Map<String, QuanExchangeConfig> configMap = new HashMap<>();

    @PostConstruct
    public void loadConfig() {
        List<QuanExchangeConfig> exchangeConfigs = quanExchangeConfigMapper.selectAll();
        exchangeConfigs.forEach(e -> {
            configMap.put(e.getExchangeId() + e.getBaseCoin() + e.getQuoteCoin(), e);
        });
        logger.info("加载交易所配置完成，配置数量：{}", exchangeConfigs.size());
    }

    public static QuanExchangeConfig getExchangeConfig(int exchangeId, String baseCoin, String quotCoin) {
        return configMap.get(exchangeId + baseCoin + quotCoin);
    }

}
