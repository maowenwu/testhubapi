package com.huobi.contract.index.contract.index.common;

public enum ExchangeEnum {
    BITSTAMP("bitstamp","https://www.bitstamp.net/api/v2/ticker/<symbol>"),
    GDAX("gdax","https://api.gdax.com/products/<symbol>/ticker"),
    BITFINEX("bitfinex","https://api.bitfinex.com/v2/tickers?symbols=<symbol>"),
    KRAKEN("kraken","https://api.kraken.com/0/public/Ticker?pair=<symbol>"),
    GEMINI("gemini","https://api.gemini.com/v1/pubticker/<symbol>"),
    HUOBI("huobi","https://api.huobi.pro/market/trade?symbol=<symbol>"),
    POLONIEX("poloniex","https://poloniex.com/public?command=returnTicker"),
    BITTREX("bittrex","https://bittrex.com/api/v1.1/public/getticker?market=<symbol>"),
    CURRENCYLAYER("currencylayer","http://www.apilayer.net/api/live?access_key=a8b26c70b1975c8daeba1a357d6b99e4");

    private String exchangeName;
    private String url;

    ExchangeEnum(String exchangeName, String url) {
        this.exchangeName = exchangeName;
        this.url = url;
    }
    public String getExchangeName() {
        return exchangeName;
    }
    private void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
    public String getUrl() {
        return url;
    }
    private void setUrl(String url) {
        this.url = url;
    }
}
