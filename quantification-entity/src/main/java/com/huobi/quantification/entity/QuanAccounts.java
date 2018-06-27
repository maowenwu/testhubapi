package com.huobi.quantification.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class QuanAccounts implements Serializable {
    private Long id;

    private Long exchangeId;

    private Long userId;

    private Long exchangeAccountsId;

    private String state;

    private String type;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExchangeAccountsId() {
        return exchangeAccountsId;
    }

    public void setExchangeAccountsId(Long exchangeAccountsId) {
        this.exchangeAccountsId = exchangeAccountsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}