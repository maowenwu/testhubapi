package com.huobi.contract.index.ws.bitfinex;

public class EOSUSDWebSocketClient extends AbstractBitfinexWSClient {


    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"EOSUSD\"}");
    }

    @Override
    protected String getIndexSymbol() {
        return "EOS-USD";
    }

}
