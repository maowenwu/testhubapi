package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanOrder {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * 账户 ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long orderAccountId;

    /**
     * 订单数量
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long orderAmount;

    /**
     * 订单撤销时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date orderCanceledAt;

    /**
     * 订单创建时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date orderCreatedAt;

    /**
     * 已成交数量
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long orderFieldAmount;

    /**
     * 已成交总金额
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long orderFieldCashAmount;

    /**
     * 已成交手续费
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal orderFieldFees;

    /**
     * 订单变为终结态的时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date orderFinishedAt;

    /**
     * 订单ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long orderOrderId;

    /**
     * 价格
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal orderPrice;

    /**
     * 订单来源
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String orderSource;

    /**
     * 订单状态
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String orderState;

    /**
     * 交易对
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String orderSymbol;

    /**
     * 订单类型
     * @mbg.generated 2018-06-28 14:50:45
     */
    private String orderType;

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

    public BigDecimal getOrderFieldFees() {
        return orderFieldFees;
    }

    public void setOrderFieldFees(BigDecimal orderFieldFees) {
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

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
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