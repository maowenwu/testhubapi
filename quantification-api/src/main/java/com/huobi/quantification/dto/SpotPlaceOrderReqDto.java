package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpotPlaceOrderReqDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1123551434598744637L;
	private int exchangeId;//交易所id
	private long accountId;//用户id
	private String baseCoin;//基础货币
	private String quoteCoin;//定价货币
	private String side;//交易方向
	private String orderType;//订单类型
	private BigDecimal price;//下单价格
	private BigDecimal quantity;//下单数量
	private BigDecimal cashAmount;//下单金额（市价单用）
	private long linkOrderId;//关联订单id
	private boolean sync = true;//异步调用
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
	public boolean isSync() {
		return sync;
	}
	public void setSync(boolean sync) {
		this.sync = sync;
	}
}
