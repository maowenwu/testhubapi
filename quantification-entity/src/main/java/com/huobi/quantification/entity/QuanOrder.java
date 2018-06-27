package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanOrder implements Serializable {
    private Long id;

    private Long orderAccountId;

    private Long orderAmount;

    private Date orderCanceledAt;

    private Date orderCreatedAt;

    private Long orderFieldAmount;

    private Long orderFieldCashAmount;

    private Long orderFieldFees;

    private Date orderFinishedAt;

    private Long orderOrderId;

    private Long orderPrice;

    private String orderSource;

    private String orderState;

    private String orderSymbol;

    private String orderType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderAccountId() {
        return orderAccountId;
    }

    public void setOrderAccountId(Long orderAccountId) {
        this.orderAccountId = orderAccountId;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderCanceledAt() {
        return orderCanceledAt;
    }

    public void setOrderCanceledAt(Date orderCanceledAt) {
        this.orderCanceledAt = orderCanceledAt;
    }

    public Date getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(Date orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public Long getOrderFieldAmount() {
        return orderFieldAmount;
    }

    public void setOrderFieldAmount(Long orderFieldAmount) {
        this.orderFieldAmount = orderFieldAmount;
    }

    public Long getOrderFieldCashAmount() {
        return orderFieldCashAmount;
    }

    public void setOrderFieldCashAmount(Long orderFieldCashAmount) {
        this.orderFieldCashAmount = orderFieldCashAmount;
    }

    public Long getOrderFieldFees() {
        return orderFieldFees;
    }

    public void setOrderFieldFees(Long orderFieldFees) {
        this.orderFieldFees = orderFieldFees;
    }

    public Date getOrderFinishedAt() {
        return orderFinishedAt;
    }

    public void setOrderFinishedAt(Date orderFinishedAt) {
        this.orderFinishedAt = orderFinishedAt;
    }

    public Long getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(Long orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public Long getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Long orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderSymbol() {
        return orderSymbol;
    }

    public void setOrderSymbol(String orderSymbol) {
        this.orderSymbol = orderSymbol;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}