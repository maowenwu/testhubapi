package com.huobi.quantification.dto;

import java.io.Serializable;

public class FutureCancelOrder implements Serializable {

    private int exchangeId;
    private long accountId;

    private Long innerOrderId;
    private Long exOrderId;
    private Long linkOrderId;
    private String baseCoin;
    private String quoteCoin;

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

    public Long getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(Long innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public Long getExOrderId() {
        return exOrderId;
    }

    public void setExOrderId(Long exOrderId) {
        this.exOrderId = exOrderId;
    }

    public Long getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(Long linkOrderId) {
        this.linkOrderId = linkOrderId;
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
}
