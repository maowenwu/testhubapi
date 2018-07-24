package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FuturePlaceOrderReqDto implements Serializable {
    private int exchangeId;
    private long accountId;
    private String baseCoin;
    private String quoteCoin;
    private String contractType;
    private String contractCode;
    // 买入1，卖出2
    private int side;
    // 开仓1，平仓2
    private int offset;
    // limit, market, ioc, limit-maker
    private String orderType;
    private BigDecimal price;
    // 下单数量
    private BigDecimal quantity;
    // 杠杆倍数
    private int lever;
    // 关联订单ID（为流动性系统内部订单ID）
    private Long linkOrderId;
    // 是否同步调用
    private boolean sync;


    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public int getLever() {
        return lever;
    }

    public void setLever(int lever) {
        this.lever = lever;
    }

    public Long getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(Long linkOrderId) {
        this.linkOrderId = linkOrderId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
