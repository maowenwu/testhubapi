package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpotBatchOrder implements Serializable{
	private String baseCoin;
	private String quoteCoin;
	private String side;
	private String orderType;
	private BigDecimal price;
	private BigDecimal quantity;
	private BigDecimal cashAmount;
	private long linkOrderId;
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
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
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
	public BigDecimal getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}
	public long getLinkOrderId() {
		return linkOrderId;
	}
	public void setLinkOrderId(long linkOrderId) {
		this.linkOrderId = linkOrderId;
	}
}
