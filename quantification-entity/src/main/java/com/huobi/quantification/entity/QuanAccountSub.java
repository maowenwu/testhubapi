package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class QuanAccountSub {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long accountId;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal balance;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String currency;

    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}