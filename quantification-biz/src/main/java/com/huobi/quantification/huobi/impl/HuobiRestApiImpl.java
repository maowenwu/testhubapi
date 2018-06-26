package com.huobi.quantification.huobi.impl;

import java.io.IOException;


import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import com.okcoin.rest.HttpUtilManager;
import com.okcoin.rest.StringUtil;
import com.huobi.quantification.huobi.HuobiRestApi;
@Service("huobiRestApi")
public class HuobiRestApiImpl implements HuobiRestApi{/*

	private String secret_key;
	
	private String api_key;
	
	private String url_prex;
	
	
	public HuobiRestApiImpl(String url_prex,String api_key,String secret_key){
		this.api_key = api_key;
		this.secret_key = secret_key;
		this.url_prex = url_prex;
	}
	*//**
	 * 获取聚合行情L
	 *//*
	private final String TICKER_URL = "/market/detail/merged?";
	@Override
	public String ticker(String symbol) throws HttpException, IOException {
		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		String param = "";
		if(!StringUtil.isEmpty(symbol )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;
		}
		String result = httpUtil.requestHttpGet(url_prex, TICKER_URL, param);
	    return result;
	}
	
	*//**
	 * 现货行情URL
	 *//*
	private final String KLINE_URL = "/market/history/kline?";
	public String kline(String symbol,String period,String size) throws HttpException, IOException{
		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		String param = "";
		if(!StringUtil.isEmpty(symbol )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;		
		}
		if(!StringUtil.isEmpty(period )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "period=" + period;		
		}
		if(!StringUtil.isEmpty(size )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "size=" + size;		
		}
		
		String result = httpUtil.requestHttpGet(url_prex, KLINE_URL, param);
	    return result;
	}
	private final String TICKERS_URL = "/market/tickers?";
	 public String tickers() throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			
			String result = httpUtil.requestHttpGet(url_prex, TICKERS_URL, param);
		    return result;
	 }
	 private final String DEPTH_URL = "/market/depth?";
	 public String depth(String symbol,String type) throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			if(!StringUtil.isEmpty(symbol )) {
				if (!param.equals("")) {
					param += "&";
				}
				param += "symbol=" + symbol;		
			}
			if(!StringUtil.isEmpty(type )) {
				if (!param.equals("")) {
					param += "&";
				}
				param += "type=" + type;		
			}
			String result = httpUtil.requestHttpGet(url_prex, DEPTH_URL, param);
		    return result;
	 }
	 private final String  ACCOUNTURL= "/v1/account/accounts/{account-id}/balance";
	 public String accounts(String accountId)throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			ACCOUNTURL.replace("{account-id}", accountId);
			String result = httpUtil.requestHttpGet(url_prex, ACCOUNTURL, param);
		    return result;
	 }
*/}

