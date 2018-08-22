package com.huobi.quantification.dto;

import java.io.Serializable;

public class FutureReplenishOrderReqDto implements Serializable {

    private int exchangeId;
    private Long accountId;
    private String baseCoin;


    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

}
