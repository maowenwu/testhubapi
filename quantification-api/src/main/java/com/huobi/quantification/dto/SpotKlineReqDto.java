package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 
 * @author lichenyang
 * @since 2018年7月16日
 */
public class SpotKlineReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1074586049702940416L;
	private int exchangeId;//交易所id
	private String baseCoin;//基础货币
	private String quoteCoin;//定价货币
	private String period;//k线类型
	private int size;//数量
	private boolean includeNow = false;//是否包含最近一根k线
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
