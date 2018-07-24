package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 撤销活跃订单
 * 
 * @author maowenwu
 */
public class SpotActiveOrderCancelReqDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int exchangeID;// 交易所ID
	private Long accountID;// 账户ID
	private String baseCoin;
	private String QuoteCoin;
	private boolean parallel=true;
	private Long timeInterval;
	private boolean sync;

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

	public boolean isParallel() {
		return parallel;
	}

	public void setParallel(boolean parallel) {
		this.parallel = parallel;
	}

	public Long getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Long timeInterval) {
		this.timeInterval = timeInterval;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
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



}
