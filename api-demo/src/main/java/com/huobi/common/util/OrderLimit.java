package com.huobi.common.util;

import java.math.BigDecimal;

public class OrderLimit {

	 private BigDecimal sell;
	 
	 private BigDecimal buy;
	 
	 private BigDecimal highLimit;
	 
	 private BigDecimal lowlimit;
	 
	 private String contractCode;
	 
	 private String  contractType;
	 
	 private String  symbol;

	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	public BigDecimal getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(BigDecimal highLimit) {
		this.highLimit = highLimit;
	}

	public BigDecimal getLowlimit() {
		return lowlimit;
	}

	public void setLowlimit(BigDecimal lowlimit) {
		this.lowlimit = lowlimit;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	 
	 
}
