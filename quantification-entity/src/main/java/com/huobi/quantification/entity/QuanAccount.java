package com.huobi.quantification.entity;

public class QuanAccount {
    /**
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Integer exchangeId;

    /**
     * 用户id
     * @mbg.generated 2018-08-03 16:47:13
     */
    private Long accountSourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
    }
}