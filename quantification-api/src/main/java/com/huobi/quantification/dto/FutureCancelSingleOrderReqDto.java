package com.huobi.quantification.dto;

import java.io.Serializable;
import java.util.List;

public class FutureCancelSingleOrderReqDto implements Serializable {
    private int exchangeId;
    private long accountId;
    private String baseCoin;
    private String quoteCoin;
    private String contractType;
    private Long exOrderId;

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Long getExOrderId() {
        return exOrderId;
    }

    public void setExOrderId(Long exOrderId) {
        this.exOrderId = exOrderId;
    }
}
