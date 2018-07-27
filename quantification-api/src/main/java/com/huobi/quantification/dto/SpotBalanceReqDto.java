package com.huobi.quantification.dto;

import java.io.Serializable;

public class SpotBalanceReqDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 167270392398573412L;
	private int exchangeId;//交易所id
    private long accountId;//用户id
    private long timeout = 100;//失效时限
    private long maxDelay = 1000;//最大延时
    
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
