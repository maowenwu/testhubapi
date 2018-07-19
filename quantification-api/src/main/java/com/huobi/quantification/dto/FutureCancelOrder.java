package com.huobi.quantification.dto;

import java.io.Serializable;

public class FutureCancelOrder implements Serializable {

    private int exchangeId;
    private long accountId;

    private String innerOrderId;
    private String exOrderId;
    private String linkOrderId;
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

    public String getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(String innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public String getExOrderId() {
        return exOrderId;
    }

    public void setExOrderId(String exOrderId) {
        this.exOrderId = exOrderId;
    }

    public String getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(String linkOrderId) {
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
