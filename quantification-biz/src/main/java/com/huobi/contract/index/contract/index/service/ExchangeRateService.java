package com.huobi.contract.index.contract.index.service;

import com.huobi.contract.index.entity.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal getLastExchangeRatePrice(String symbol);

    ExchangeRate getLastExchangeRate(String symbol);

    BigDecimal getUsdCnyExchangeRatePrice();

    BigDecimal getUsdtUsdExchangeRatePrice();
}
