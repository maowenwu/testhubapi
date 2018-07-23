package com.huobi.quantification.huobi.response;

import java.util.Date;

/**
 * 
 * @author shaoxiaofeng
 * @since 2018/6/29
 * 
 */
public class Trade {

	/**
	 * id : 600848670 ts : 1489464451000 data :
	 * [{"id":600848670,"price":7962.62,"amount":0.0122,"direction":"buy","ts":1489464451000}]
	 */

	private Long id;
	private Date ts;
	private TradeDetail data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public TradeDetail getData() {
		return data;
	}

	public void setData(TradeDetail data) {
		this.data = data;
	}
}
