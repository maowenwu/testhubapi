package com.huobi.contract.index.dto;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;


public class CurrencylayerExchangeRateResp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3067783295963924572L;
	private Boolean success;
	private String terms;
	private String privacy;
	private Date timestamp;
	private String source;
	private JSONObject quotes;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public JSONObject getQuotes() {
		return quotes;
	}
	public void setQuotes(JSONObject quotes) {
		this.quotes = quotes;
	}
	
}
