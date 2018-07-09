package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanOrderMatchResult {
	/**
	 * 订单成交时间
	 */
	private Date orderCreatedAt;

	/**
	 * 订单成交数量
	 */
	private BigDecimal orderFilledAmount;

	/**
	 * 订单成交手续费
	 */
	private BigDecimal orderFilledFees;

	/**
	 * 订单id
	 */
	private long orderId;

	/**
	 * 撮合id
	 */
	private long matchId;

	/**
	 * 订单成交记录id
	 */
	private long matchResultId;

	/**
	 * 订单成交价格
	 */
	private BigDecimal orderPrice;

	/**
	 * 订单来源
	 */
	private String orderSource;

	/**
	 * 交易对
	 */
	private String orderSymbol;

	/**
	 * 订单类型
	 */
	private String orderType;

	public Date getOrderCreatedAt() {
		return orderCreatedAt;
	}

	public void setOrderCreatedAt(Date orderCreatedAt) {
		this.orderCreatedAt = orderCreatedAt;
	}

	public BigDecimal getOrderFilledAmount() {
		return orderFilledAmount;
	}

	public void setOrderFilledAmount(BigDecimal orderFilledAmount) {
		this.orderFilledAmount = orderFilledAmount;
	}

	public BigDecimal getOrderFilledFees() {
		return orderFilledFees;
	}

	public void setOrderFilledFees(BigDecimal orderFilledFees) {
		this.orderFilledFees = orderFilledFees;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public long getMatchResultId() {
		return matchResultId;
	}

	public void setMatchResultId(long matchResultId) {
		this.matchResultId = matchResultId;
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
