package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 指数图表历史价格结果对象
 * @see #exchangeName
 * @see #exchangeShortName
 * @see #list
 */
public class TargetHisPriceListResult implements Serializable {
    private static final long serialVersionUID = -5040691838671514215L;
    /**
     * 交易所名称
     */
    private String exchangeName;
    /**
     *交易所简称
     */
    private String exchangeShortName;
    /**
     *交易所历史时间点和对应价格信息
     */
    private List<ExchangePrice> list;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }

    public void setExchangeShortName(String exchangeShortName) {
        this.exchangeShortName = exchangeShortName;
    }

    public List<ExchangePrice> getList() {
        return list;
    }

    public void setList(List<ExchangePrice> list) {
        this.list = list;
    }
}
