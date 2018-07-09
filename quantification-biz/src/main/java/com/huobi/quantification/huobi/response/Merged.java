package com.huobi.quantification.huobi.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author shaoxiaofeng
 * @since 2018/6/29
 */

public class Merged {

	/**
	 * id : 1499225271 ts : 1499225271000 close : 1885 open : 1960 high : 1985 low :
	 * 1856 amount : 81486.2926 count : 42122 vol : 1.57052744857082E8 ask :
	 * [1885,21.8804] bid : [1884,1.6702]
	 */

	private int id;
	private long ts;
	private BigDecimal close;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal amount;
	private BigDecimal count;
	private BigDecimal vol;
	private List<BigDecimal> ask;
	private List<BigDecimal> bid;
	private String ch;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public BigDecimal getVol() {
		return vol;
	}

	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}

	public List<BigDecimal> getAsk() {
		return ask;
	}

	public void setAsk(List<BigDecimal> ask) {
		this.ask = ask;
	}

	public List<BigDecimal> getBid() {
		return bid;
	}

	public void setBid(List<BigDecimal> bid) {
		this.bid = bid;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

}
