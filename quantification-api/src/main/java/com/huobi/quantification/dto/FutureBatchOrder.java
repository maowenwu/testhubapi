package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FutureBatchOrder implements Serializable {

    private String baseCoin;
    private String quoteCoin;
    private String contractType;
    private String contractCode;
    private Integer side;
    private Integer offset;
    private String orderType;
    private BigDecimal price;
    private BigDecimal quantity;
    private Integer lever;
    private Long linkOrderId;

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

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
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

    public Integer getLever() {
        return lever;
    }

    public void setLever(Integer lever) {
        this.lever = lever;
    }

    public Long getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(Long linkOrderId) {
        this.linkOrderId = linkOrderId;
    }
}
