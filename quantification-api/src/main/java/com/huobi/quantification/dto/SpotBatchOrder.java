package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpotBatchOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5205468339575483277L;
	private String baseCoin;//基础货币
	private String quoteCoin;//定价货币
	private String side;//数量
	private String orderType;//订单类型
	private BigDecimal price;//订单价格
	private BigDecimal quantity;//数量
	private BigDecimal cashAmount;//金额（市价单用）
	private long linkOrderId;//关联订单id
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
