package com.huobi.quantification.entity;

public class QuanAccountFuture {
    /**
     * @mbg.generated 2018-08-17 16:39:10
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-08-17 16:39:10
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-08-17 16:39:10
     */
    private String accountName;

    /**
     * 用户id
     * @mbg.generated 2018-08-17 16:39:10
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getAccountSourceId() {
        return accountSourceId;
    }

    public void setAccountSourceId(Long accountSourceId) {
        this.accountSourceId = accountSourceId;
    }
}