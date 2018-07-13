package com.huobi.quantification.huobi.response;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author lichenyang
 * @since  2018年7月13日
 */
public class TradeDetail {
	
	private Long id;
	private BigDecimal price;
	private BigDecimal amount;
	private String direction;
	private Date ts;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
}
