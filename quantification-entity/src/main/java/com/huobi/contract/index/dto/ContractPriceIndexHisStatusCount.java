package com.huobi.contract.index.dto;

import java.io.Serializable;

/**
 * @desc
 * @Author mingjianyong
 */
public class ContractPriceIndexHisStatusCount implements Serializable {

    private static final long serialVersionUID = -2420932028383391081L;
    private Long exchangeId;
    private String targetSymbol;
    private Long failCount;
    private Long succCount;

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public void setTargetSymbol(String targetSymbol) {
        this.targetSymbol = targetSymbol;
    }

    public Long getFailCount() {
        return failCount;
    }

    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }

    public Long getSuccCount() {
        return succCount;
    }

    public void setSuccCount(Long succCount) {
        this.succCount = succCount;
    }
}
