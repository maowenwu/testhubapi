package com.okcoin.rest;

import java.io.IOException;

import com.huobi.contract.index.common.util.RestTemplateUtil;
import com.huobi.contract.index.entity.ProxyConfig;
import com.huobi.quantification.huobi.impl.HuobiRestApiImpl;
import org.apache.http.HttpException;

public class HuobiClient {
	public static void main(String[] args) throws HttpException, IOException {

		String api_key = "e26abcfe-8a11-45e7-9c01-fd3585913771"; // OKCoin申请的apiKey
		String secret_key = "DBC9485DBFBFA73F2CC79EA04C4A612D"; // OKCoin 申请的secret_key
		String url_prex = "https://api.huobipro.com"; // 注
		
		/*HuobiRestApiImpl api=new HuobiRestApiImpl(url_prex,api_key,secret_key);
		String str=api.ticker("btcusdt");
		System.out.println("str------"+str);*/
		/*
		String str1=api.kline("btcusdt", "1day", "200");
		System.out.println("str1----------"+str1);
		
		String str2=api.depth("btcusdt","step5");
		System.out.println("str2----------"+str2);*/
		/*
		ProxyConfig proxyConfig=new ProxyConfig();
		proxyConfig.setUsername("jp1");
		proxyConfig.setHost("54.248.65.254");
		proxyConfig.setPort(13128);
		proxyConfig.setProxyType("2"); 
		String result = RestTemplateUtil.getInstance(proxyConfig).get("https://api.huobipro.com/market/detail/merged?symbol=btcusdt");
		System.out.println(result);*/
		
		
	}
}
