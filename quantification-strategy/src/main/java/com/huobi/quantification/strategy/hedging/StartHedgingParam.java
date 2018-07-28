package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;

public class StartHedgingParam {

	private String baseCoin;
	private String quoteCoin;
	private Long spotAccountID;//处理现货账户id
	private Integer spotExchangeId;//现货交易所id
	private Long futureAccountID;//处理期货账户id
	private Integer futureExchangeId;//期货交易所id
	private BigDecimal slippage;//滑点
	private BigDecimal feeRate;//手续费率
	private String contractCode;//合约code
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
	public int getSpotExchangeId() {
		return spotExchangeId;
	}
	public void setSpotExchangeId(int spotExchangeId) {
		this.spotExchangeId = spotExchangeId;
	}
	public Long getFutureAccountID() {
		return futureAccountID;
	}
	public void setFutureAccountID(Long futureAccountID) {
		this.futureAccountID = futureAccountID;
	}
	public Integer getFutureExchangeId() {
		return futureExchangeId;
	}
	public void setFutureExchangeId(Integer futureExchangeId) {
		this.futureExchangeId = futureExchangeId;
	}
	public BigDecimal getSlippage() {
		return slippage;
	}
	public void setSlippage(BigDecimal slippage) {
		this.slippage = slippage;
	}
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	

	
}
