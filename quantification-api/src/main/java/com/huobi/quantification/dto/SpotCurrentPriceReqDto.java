package com.huobi.quantification.dto;

import java.io.Serializable;

public class SpotCurrentPriceReqDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1231522090667031692L;
	private int exchangeId;//交易所id
	private String baseCoin;//基础货币
	private String quoteCoin;//定价货币
	private long timeout = 100;//失效时间
	private long maxDelay = 1000;//最大延时
	public int getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(int exchangeId) {
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
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public long getMaxDelay() {
		return maxDelay;
	}
	public void setMaxDelay(long maxDelay) {
		this.maxDelay = maxDelay;
	}
	
	
}
