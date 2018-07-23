package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanTrade {
    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private Long id;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private String symbol;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private Long queryId;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private BigDecimal price;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private BigDecimal amount;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private String direction;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private Date ts;

    /**
     * @mbg.generated 2018-07-23 11:19:16
     */
    private Long tradeId;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }
}