package com.huobi.quantification.dto;

import java.io.Serializable;
import java.util.List;

public class FutureQueryOrderLinkReqDto implements Serializable {

    private int exchangeId;
    private long accountId;
    private List<Long> linkOrderId;
    private String baseCoin;
    private String quoteCoin;
    private String contractType;
    private String contractCode;
    private long maxDelay;


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

    public List<Long> getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(List<Long> linkOrderId) {
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

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public long getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
    }
}
