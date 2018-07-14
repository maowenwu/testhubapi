package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 查询订单-根据交易所orderid
 * 
 * @author maowenwu
 *
 */
public class SpotOrderReqExchangeDto implements Serializable {

	private static final long serialVersionUID = -4127302173955071604L;

	private Long exchangeID;// 交易所ID
	private Long accountID;// 账户ID
	private Long[] exOrderID;// 数组
	private String baseCoin;
	private String QuoteCoin;
	private Long timeout;
	private Long maxDelay;

	public Long getExchangeID() {
		return exchangeID;
	}

	public void setExchangeID(Long exchangeID) {
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

	public Long[] getExOrderID() {
		return exOrderID;
	}

	public void setExOrderID(Long[] exOrderID) {
		this.exOrderID = exOrderID;
	}

}
