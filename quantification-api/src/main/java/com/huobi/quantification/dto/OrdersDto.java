package com.huobi.quantification.dto;

import java.io.Serializable;

public class OrdersDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accountId;//账户 ID，使用accounts方法获得。币币交易使用‘spot’账户的accountid；借贷资产交易，请使用‘margin’账户的accountid
	
	private String amount;//限价单表示下单数量，市价买单时表示买多少钱，市价卖单时表示卖多少币
	
	private String price;//限价单表示下单数量，市价买单时表示买多少钱，市价卖单时表示卖多少币
	
	private String source;//下单价格，市价单不传该参数
	
	private String symbol;//订单来源
	
	private String type;//订单类型

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
