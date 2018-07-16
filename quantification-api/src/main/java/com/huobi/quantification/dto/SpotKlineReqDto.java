package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 
 * @author lichenyang
 * @since 2018年7月16日
 */
public class SpotKlineReqDto implements Serializable {
	private int exchangeId;
	private String baseCoin;
	private String quoteCoin;
	private String period;
	private int size;
	private boolean includeNow;
	private long timeout;
	private long maxDelay;
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
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isIncludeNow() {
		return includeNow;
	}
	public void setIncludeNow(boolean includeNow) {
		this.includeNow = includeNow;
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
