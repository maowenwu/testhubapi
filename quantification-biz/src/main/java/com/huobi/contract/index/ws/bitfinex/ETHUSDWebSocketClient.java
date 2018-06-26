package com.huobi.contract.index.ws.bitfinex;

public class ETHUSDWebSocketClient extends AbstractBitfinexWSClient {

    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"ETHUSD\"}");
    }

    @Override
    protected String getIndexSymbol() {
        return "ETH-USD";
    }

}
