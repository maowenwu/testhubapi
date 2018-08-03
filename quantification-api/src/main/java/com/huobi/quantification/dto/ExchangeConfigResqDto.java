package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExchangeConfigResqDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer exchangeId;
	private String baseCoin;
	private String quoteCoin;
	private Integer pricePrecision;
	private Integer amountPrecision;
	private BigDecimal faceValue;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
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

	public Integer getPricePrecision() {
		return pricePrecision;
	}

	public void setPricePrecision(Integer pricePrecision) {
		this.pricePrecision = pricePrecision;
	}

	public Integer getAmountPrecision() {
		return amountPrecision;
	}

	public void setAmountPrecision(Integer amountPrecision) {
		this.amountPrecision = amountPrecision;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
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
}