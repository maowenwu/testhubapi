package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanTrade {
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
     * 交易对
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String symbol;

    /**
     * 查询id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long queryId;

    /**
     * 价格
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal price;

    /**
     * 数量
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal amount;

    /**
     * 描述
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String direction;

    /**
     * 响应时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date ts;

    /**
     * 成交id
     * @mbg.generated 2018-07-27 14:41:01
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