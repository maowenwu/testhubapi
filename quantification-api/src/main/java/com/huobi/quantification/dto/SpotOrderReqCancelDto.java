package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 撤销订单-根据内部orderID
 * 
 * @author maowenwu
 */
public class SpotOrderReqCancelDto implements Serializable {

	private static final long serialVersionUID = 1971969867040987743L;

	private int exchangeID;// 交易所ID
	private Long accountID;// 账户ID
	private boolean parallel;
	private Long timeInterval;
	private boolean sync;

	class Orders {
		private int innerOrderID;
		private Long exOrderID;
		private int linkOrderID;
		private String baseCoin;
		private String QuoteCoin;

		public int getInnerOrderID() {
			return innerOrderID;
		}

		public void setInnerOrderID(int innerOrderID) {
			this.innerOrderID = innerOrderID;
		}

		public Long getExOrderID() {
			return exOrderID;
		}

		public void setExOrderID(Long exOrderID) {
			this.exOrderID = exOrderID;
		}

		public int getLinkOrderID() {
			return linkOrderID;
		}

		public void setLinkOrderID(int linkOrderID) {
			this.linkOrderID = linkOrderID;
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

}
