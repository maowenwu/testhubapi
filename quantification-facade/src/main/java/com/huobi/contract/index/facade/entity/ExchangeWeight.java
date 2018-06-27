package com.huobi.contract.index.facade.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易所权重变更记录对象
 */
public class ExchangeWeight implements Serializable {

    private static final long serialVersionUID = 1420532523611685940L;
    /**
     *交易所ID
     */
    private Long exchangeId;
    /**
     *交易所全称
     */
    private String exchangeName;
    /**
     *交易所简称
     */
    private String exchangeShortName;
    /**
     *初始权重
     */
    private BigDecimal initialWeight;
    /**
     *变更前权重
     */
    private BigDecimal beforeChangeWeight;
    /**
     *变更后权重
     */
    private BigDecimal afterChangeWeight;

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

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

    public BigDecimal getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(BigDecimal initialWeight) {
        this.initialWeight = initialWeight;
    }

    public BigDecimal getBeforeChangeWeight() {
        return beforeChangeWeight;
    }

    public void setBeforeChangeWeight(BigDecimal beforeChangeWeight) {
        this.beforeChangeWeight = beforeChangeWeight;
    }

    public BigDecimal getAfterChangeWeight() {
        return afterChangeWeight;
    }

    public void setAfterChangeWeight(BigDecimal afterChangeWeight) {
        this.afterChangeWeight = afterChangeWeight;
    }
}
