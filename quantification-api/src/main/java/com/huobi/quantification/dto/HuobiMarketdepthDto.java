package com.huobi.quantification.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class HuobiMarketdepthDto {

	private String status;
	
	private String ch;
	
	private Date ts;
	
	private JSONObject tick;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public JSONObject getTick() {
		return tick;
	}

	public void setTick(JSONObject tick) {
		this.tick = tick;
	}
	
	
}
