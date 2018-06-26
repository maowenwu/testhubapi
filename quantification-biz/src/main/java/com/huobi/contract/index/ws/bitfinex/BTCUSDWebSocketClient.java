package com.huobi.contract.index.ws.bitfinex;

public class BTCUSDWebSocketClient extends AbstractBitfinexWSClient {

    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"BTCUSD\"}");
    }


    @Override
    protected String getIndexSymbol() {
        return "BTC-USD";
    }
}
