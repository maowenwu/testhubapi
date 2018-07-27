package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;

public class StartHedgingParam {

	private String baseCoin;
	private String quoteCoin;
	private Long spotAccountID;
	private int spotExchangeId;
	private Long furureAccountID;
	private Long furureExchangeId;
	private BigDecimal slippage;//滑点
	private BigDecimal feeRate;//手续费率


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

	public Long getSpotAccountID() {
		return spotAccountID;
	}

	public void setSpotAccountID(Long spotAccountID) {
		this.spotAccountID = spotAccountID;
	}

	public Long getFurureAccountID() {
		return furureAccountID;
	}

	public void setFurureAccountIDB(Long furureAccountID) {
		this.furureAccountID = furureAccountID;
	}

	public int getSpotExchangeId() {
		return spotExchangeId;
	}

	public void setSpotExchangeId(int spotExchangeId) {
		this.spotExchangeId = spotExchangeId;
	}

	public Long getFurureExchangeId() {
		return furureExchangeId;
	}

	public void setFurureExchangeId(Long furureExchangeId) {
		this.furureExchangeId = furureExchangeId;
	}

	public BigDecimal getSlippage() {
		return slippage;
	}

	public void setSlippage(BigDecimal slippage) {
		this.slippage = slippage;
	}

	public void setFurureAccountID(Long furureAccountID) {
		this.furureAccountID = furureAccountID;
	}

	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

}
