package com.huobi.quantification.entity;

public class QuanAccount {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long userId;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String accountsType;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String accountsName;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String accessKey;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String secretKey;

    /**
     * @mbg.generated 2018-06-28 14:50:45
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}