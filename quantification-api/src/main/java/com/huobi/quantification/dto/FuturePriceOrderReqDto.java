package com.huobi.quantification.dto;

import java.io.Serializable;

public class FuturePriceOrderReqDto implements Serializable {

    private Integer exchangeId;
    private Long accountId;


    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
