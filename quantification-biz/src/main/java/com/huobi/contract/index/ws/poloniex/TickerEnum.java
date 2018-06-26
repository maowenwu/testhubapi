package com.huobi.contract.index.ws.poloniex;

public enum TickerEnum {


    ETC_BTC(171, "ETC-USD"), XRP_BTC(117, "XRP-USD");

    private int id;

    private String symbol;

    TickerEnum(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static TickerEnum valueOfInt(int id) {
        for (TickerEnum ticker : values()) {
            if (ticker.id == id) {
                return ticker;
            }
        }
        return null;
    }

}
