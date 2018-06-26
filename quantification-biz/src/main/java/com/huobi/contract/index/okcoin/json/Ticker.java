package com.huobi.contract.index.okcoin.json;

import java.math.BigDecimal;

public class Ticker {
	private BigDecimal buy;
	private BigDecimal high;
	private BigDecimal last;
	private BigDecimal low;
	private BigDecimal sell;
	private BigDecimal vol;

	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	public BigDecimal getVol() {
		return vol;
	}

	public void setVol(BigDecimal vol) {
		this.vol = vol;
	}

	private BigDecimal usdt = BigDecimal.valueOf(6.3);
	private BigDecimal rate = usdt;
	
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "Ticker [buy=" + buy + ", high=" + high + ", last=" + last + ", low=" + low + ", sell=" + sell + ", vol="
				+ vol + "]";
	}

	public String toFormString() {
		return "人民币 : <br/>买一价 : " + buy.multiply(rate) + " CNY<br/> 最高价 : " + high.multiply(rate) + " CNY<br/><span style=\"color:red\">最新成交价:"
				+ last.multiply(rate) + " CNY</span><br/>最低价 : " + low.multiply(rate) + " CNY<br/>卖一价 : " + sell.multiply(rate)
				+ " CNY<br/> 成交量(最近的24小时) : " + vol.multiply(rate) + " CNY<br/> USDT : <br/>买一价 : " + buy + "<br/> 最高价 : " + high
				+ "<br/>最新成交价 : " + last + "<br/>最低价 : " + low + "<br/>卖一价:" + sell + "<br/> 成交量(最近的24小时) : " + vol;
	}
}