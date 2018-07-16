package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 查询订单-根据订单状态
 * 
 * @author maowenwu
 */
public class SpotOrderReqStatusDto implements Serializable {

	private static final long serialVersionUID = 1971969867040987743L;

	private int exchangeID;// 交易所ID
	private Long accountID;// 账户ID
	private String status;
	private String baseCoin;
	private String QuoteCoin;
	private Long timeout;
	private Long maxDelay;

	public int getExchangeID() {
		return exchangeID;
	}

	public void setExchangeID(int exchangeID) {
		this.exchangeID = exchangeID;
	}

	public Long getAccountID() {
		return accountID;
	}

	public void setAccountID(Long accountID) {
		this.accountID = accountID;
	}

	public String getBaseCoin() {
		return baseCoin;
	}

	public void setBaseCoin(String baseCoin) {
		this.baseCoin = baseCoin;
	}

	public String getQuoteCoin() {
		return QuoteCoin;
	}

	public void setQuoteCoin(String quoteCoin) {
		QuoteCoin = quoteCoin;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public Long getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(Long maxDelay) {
		this.maxDelay = maxDelay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
