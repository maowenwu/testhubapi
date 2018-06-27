package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanOrder implements Serializable {
    private Long id;

    /**
     * 账户 ID
     */
    private Long orderAccountId;

    /**
     * 订单数量
     */
    private Long orderAmount;

    /**
     * 订单撤销时间
     */
    private Date orderCanceledAt;

    /**
     * 订单创建时间
     */
    private Date orderCreatedAt;

    /**
     * 已成交数量
     */
    private Long orderFieldAmount;

    /**
     * 已成交总金额
     */
    private Long orderFieldCashAmount;

    /**
     * 已成交手续费
     */
    private Long orderFieldFees;

    /**
     * 订单变为终结态的时间
     */
    private Date orderFinishedAt;

    /**
     * 订单ID
     */
    private Long orderOrderId;

    /**
     * 价格
     */
    private Long orderPrice;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 交易对
     */
    private String orderSymbol;

    /**
     * 订单类型
     */
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