package com.huobi.contract.index.ws.bitfinex;

public class LTCUSDWebSocketClient extends AbstractBitfinexWSClient {

    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"LTCUSD\"}");
    }

    @Override
    protected String getIndexSymbol() {
        return "LTC-USD";
    }

}
