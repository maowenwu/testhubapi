package com.huobi.contract.index.contract.index.service.impl;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.contract.index.service.ExchangeRateService;
import com.huobi.contract.index.dao.ContractPriceIndexMapper;
import com.huobi.contract.index.dao.ExchangeRateMapper;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("exchangeRateService")
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    @Autowired
    private RedisService redisService;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;

    @Override
    public BigDecimal getLastExchangeRatePrice(String symbol) {
        BigDecimal rate = redisService.getIndexRate(symbol);
        if (rate != null) {
            return rate;
        }
        ContractPriceIndex indexPrice = contractPriceIndexMapper.getLastIndex(symbol);
        if(indexPrice!=null){
            return indexPrice.getIndexPrice();
        }
        ExchangeRate exchangeRate = exchangeRateMapper.getLastExchangeRateBySymbol(symbol);
        if (exchangeRate != null) {
            return exchangeRate.getExchangeRate();
        }
        return null;
    }

    @Override
    public ExchangeRate getLastExchangeRate(String symbol) {
        return exchangeRateMapper.getLastExchangeRateBySymbol(symbol);
    }

    @Override
    public BigDecimal getUsdCnyExchangeRatePrice() {
        return null;
    }

    @Override
    public BigDecimal getUsdtUsdExchangeRatePrice() {
        return null;
    }
}
