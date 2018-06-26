package com.huobi.contract.index.contract.index.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum CoinStation {

	CURRENCYLAYER("CURRENCYLAYER", "http://www.apilayer.net/api/live?1access_key=2a121148f9f518b7bc9abf507d21f800"),
	CURENCYLAYER1("CURRENCYLAYER","http://www.apilayer.net/api/live?access_key=a33e801c64459bd83ea584054f3bcc7f"),
	CURENCYLAYER2("CURRENCYLAYER","http://www.apilayer.net/api/live?access_key=7cea305cc301f37282e78910b212789e"),
	CURRENCYSOURCE("CURRENCY_SOURCE", "http://www.apilayer.net/api/live?access_key=a8b26c70b1975c8daeba1a357d6b99e4&source=<source>");
	private String name;// 名称
	private String url;// api访问地址

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	private void setUrl(String url) {
		this.url = url;
	}


	CoinStation(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public static CoinStation getInsByname(String name) {
		for (CoinStation lma : CoinStation.values()) {
			if (lma.getName().equals(name)) {
				return lma;
			}
		}
		return null;
	}

	public static List<String> getAllSource() {
		List<String> sources = new ArrayList<>();
		for (CoinStation station : CoinStation.values()) {
			sources.add(station.name);
		}
		return sources;
	}

	public static List<String> getAllCurrencyLayer(String currencyName){
		List<String> urlList = new ArrayList<>();
		for (CoinStation station : CoinStation.values()) {
			if(station.name.equals(currencyName)){
				urlList.add(station.url);
			}
		}
		return urlList;
	}
}
