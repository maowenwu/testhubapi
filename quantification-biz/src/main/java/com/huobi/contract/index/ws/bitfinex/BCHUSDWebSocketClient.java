package com.huobi.contract.index.ws.bitfinex;

public class BCHUSDWebSocketClient extends AbstractBitfinexWSClient {

    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"BCHUSD\"}");
    }


    @Override
    protected String getIndexSymbol() {
        return "BCH-USD";
    }
}
