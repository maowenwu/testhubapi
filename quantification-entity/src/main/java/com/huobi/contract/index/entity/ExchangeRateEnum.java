package com.huobi.contract.index.entity;

public enum ExchangeRateEnum {
	USDCNY("USD-CNY", "USDCNY"), USDTUSD("USDT-USD", "USDTUSD");

	private String symbolId;
	private String symboleName;

	ExchangeRateEnum(String symbolId, String symboleName) {
		this.symbolId = symbolId;
		this.symboleName = symboleName;
	}

	public String getSymbolId() {
		return symbolId;
	}

	private void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}

	public String getSymboleName() {
		return symboleName;
	}

	private void setSymboleName(String symboleName) {
		this.symboleName = symboleName;
	}

}
