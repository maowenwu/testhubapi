package com.huobi.contract.index.ws.bitfinex;

public class ETCUSDWebSocketClient extends AbstractBitfinexWSClient {


    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"ETCUSD\"}");
    }

    @Override
    protected String getIndexSymbol() {
        return "ETC-USD";
    }

}
