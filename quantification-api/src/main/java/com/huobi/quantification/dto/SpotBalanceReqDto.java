package com.huobi.quantification.dto;

import java.io.Serializable;

public class SpotBalanceReqDto implements Serializable{
	private int exchangeId;
    private long accountId;
    private long timeout;
    private long maxDelay;
    
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
