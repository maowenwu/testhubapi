package com.huobi.quantification.dto;

import java.io.Serializable;

/**
 * 撤销订单-根据内部orderID
 * 
 * @author maowenwu
 */
public class SpotOrderBatchCancelReqDto implements Serializable {

	private static final long serialVersionUID = 1971969867040987743L;

	private String accountId;// 交易所ID
	private String symbol;// 账户ID
	private String side;
	private int size;
	private String  baseCoin;
	private String  quoteCoin;
	
	
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	
	

	
}
