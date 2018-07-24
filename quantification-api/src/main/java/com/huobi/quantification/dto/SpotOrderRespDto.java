package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SpotOrderRespDto implements Serializable {

	private static final long serialVersionUID = -5541859922564105897L;

	private int exOrderID;
	private Long linkOrderID;
	private Date createTime;
	private Date updateTime;
	private Integer status;
	private String baseCoin;
	private String quoteCoin;
	private String side;// 买入or卖出
	private String orderType; // limit, market, ioc, limit-maker…
	private BigDecimal orderPrice;
	private BigDecimal dealPrice;
	private String orderQty;
	private String dealQty;
	private BigDecimal orderCashAmount;// 市价单使用
	private BigDecimal dealCashAmount;
	private String remainingQty;
	private BigDecimal fees;

	public int getExOrderID() {
		return exOrderID;
	}

	public void setExOrderID(int exOrderID) {
		this.exOrderID = exOrderID;
	}

	public Long getLinkOrderID() {
		return linkOrderID;
	}

	public void setLinkOrderID(Long linkOrderID) {
		this.linkOrderID = linkOrderID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getDealQty() {
		return dealQty;
	}

	public void setDealQty(String dealQty) {
		this.dealQty = dealQty;
	}

	public BigDecimal getOrderCashAmount() {
		return orderCashAmount;
	}

	public void setOrderCashAmount(BigDecimal orderCashAmount) {
		this.orderCashAmount = orderCashAmount;
	}

	public BigDecimal getDealCashAmount() {
		return dealCashAmount;
	}

	public void setDealCashAmount(BigDecimal dealCashAmount) {
		this.dealCashAmount = dealCashAmount;
	}

	public String getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(String remainingQty) {
		this.remainingQty = remainingQty;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

}
