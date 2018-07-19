package com.huobi.quantification.request.future;

import java.util.List;

public class FutureOkBatchOrderRequest {

    private Long accountId;

    private String symbol;
    private String contractType;
    private List<FutureOkBatchOrder> orders;
    private Integer leverRate;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public List<FutureOkBatchOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<FutureOkBatchOrder> orders) {
        this.orders = orders;
    }

    public Integer getLeverRate() {
        return leverRate;
    }

    public void setLeverRate(Integer leverRate) {
        this.leverRate = leverRate;
    }
}
