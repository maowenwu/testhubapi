package com.huobi.quantification.entity;

public class QuanAccount {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer exchangeId;

    /**
     * 用户id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long accountSourceId;

    /**
     * 用户类型
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String accountsType;

    /**
     * 用户名
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String accountsName;

    /**
     * 用户状态
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String state;

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

    public String getAccountsType() {
        return accountsType;
    }

    public void setAccountsType(String accountsType) {
        this.accountsType = accountsType;
    }

    public String getAccountsName() {
        return accountsName;
    }

    public void setAccountsName(String accountsName) {
        this.accountsName = accountsName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}