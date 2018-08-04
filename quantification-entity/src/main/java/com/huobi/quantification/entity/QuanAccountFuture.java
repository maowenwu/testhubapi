package com.huobi.quantification.entity;

public class QuanAccountFuture {
    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Long id;

    /**
     * @mbg.generated 2018-08-04 10:24:20
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-08-04 10:24:20
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