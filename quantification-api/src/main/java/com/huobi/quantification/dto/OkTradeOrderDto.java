package com.huobi.quantification.dto;

import java.math.BigDecimal;

public class OkTradeOrderDto {

    private String strategyName;
    private Long strategyVersion;
    private Long accountId;

    private String symbol;
    private String contractType;
    private String price;
    private String amount;
    private int type;
    private int matchPrice;
    private Integer leverRate;


    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Long getStrategyVersion() {
        return strategyVersion;
    }

    public void setStrategyVersion(Long strategyVersion) {
        this.strategyVersion = strategyVersion;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(int matchPrice) {
        this.matchPrice = matchPrice;
    }

    public Integer getLeverRate() {
        return leverRate;
    }

    public void setLeverRate(Integer leverRate) {
        this.leverRate = leverRate;
    }
}
