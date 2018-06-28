package com.huobi.quantification.enums;

public enum OkSymbolEnum {

    BTC_USD("btc_usd"), LTC_USD("ltc_usd"), ETH_USD("eth_usd"), ETC_USD("etc_usd"), BCH_USD("bch_usd");

    private String symbol;
    private String baseCoin;
    private String quoteCoin;

    OkSymbolEnum(String symbol) {
        this.symbol = symbol;
        String[] pars = symbol.split("_");
        baseCoin = pars[0];
        quoteCoin = pars[1];
    }

    public static OkSymbolEnum valueSymbolOf(String symbol) {
        for (OkSymbolEnum symbolEnum : values()) {
            if (symbolEnum.getSymbol().equalsIgnoreCase(symbol)) {
                return symbolEnum;
            }
        }
        throw new IllegalArgumentException("输入币对异常，该币对不支持symbol=" + symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }
}
