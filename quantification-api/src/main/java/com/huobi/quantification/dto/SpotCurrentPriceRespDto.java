package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SpotCurrentPriceRespDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4623248085162566468L;
	private Date ts;
	private BigDecimal currentPrice;
	
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
}
