package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanOrder {
    /**
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long id;

    /**
     * 火币或ok返回的订单id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long orderSourceId;

    /**
     * 交易对
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String orderSymbol;

    /**
     * 订单类型
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String orderType;

    /**
     * 订单状态
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer orderState;

    /**
     * 账户 ID
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long orderAccountId;

    /**
     * 订单数量
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal orderAmount;

    /**
     * 已成交数量
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal orderFieldAmount;

    /**
     * 已成交总金额
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal orderFieldCashAmount;

    /**
     * 已成交手续费
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal orderFieldFees;

    /**
     * 价格
     * @mbg.generated 2018-07-27 14:41:01
     */
    private BigDecimal orderPrice;

    /**
     * 订单来源
     * @mbg.generated 2018-07-27 14:41:01
     */
    private String orderSource;

    /**
     * 订单创建时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date orderCreatedAt;

    /**
     * 订单撤销时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date orderCanceledAt;

    /**
     * 订单变为终结态的时间
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Date orderFinishedAt;

    /**
     * 内部id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Long orderInnerId;

    /**
     * 交易所id
     * @mbg.generated 2018-07-27 14:41:01
     */
    private Integer exchangeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderSourceId() {
        return orderSourceId;
    }

    public void setOrderSourceId(Long orderSourceId) {
        this.orderSourceId = orderSourceId;
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

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Long getOrderAccountId() {
        return orderAccountId;
    }

    public void setOrderAccountId(Long orderAccountId) {
        this.orderAccountId = orderAccountId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderFieldAmount() {
        return orderFieldAmount;
    }

    public void setOrderFieldAmount(BigDecimal orderFieldAmount) {
        this.orderFieldAmount = orderFieldAmount;
    }

    public BigDecimal getOrderFieldCashAmount() {
        return orderFieldCashAmount;
    }

    public void setOrderFieldCashAmount(BigDecimal orderFieldCashAmount) {
        this.orderFieldCashAmount = orderFieldCashAmount;
    }

    public BigDecimal getOrderFieldFees() {
        return orderFieldFees;
    }

    public void setOrderFieldFees(BigDecimal orderFieldFees) {
        this.orderFieldFees = orderFieldFees;
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

    public Date getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(Date orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public Date getOrderCanceledAt() {
        return orderCanceledAt;
    }

    public void setOrderCanceledAt(Date orderCanceledAt) {
        this.orderCanceledAt = orderCanceledAt;
    }

    public Date getOrderFinishedAt() {
        return orderFinishedAt;
    }

    public void setOrderFinishedAt(Date orderFinishedAt) {
        this.orderFinishedAt = orderFinishedAt;
    }

    public Long getOrderInnerId() {
        return orderInnerId;
    }

    public void setOrderInnerId(Long orderInnerId) {
        this.orderInnerId = orderInnerId;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }
}