package com.huobi.contract.index.ws.bitfinex;

public class XRPUSDWebSocketClient extends AbstractBitfinexWSClient {

    @Override
    protected void handleOpen() {
        send("{\"event\": \"subscribe\",\"channel\": \"trades\",\"pair\": \"XRPBTC\"}");
    }


    @Override
    protected String getIndexSymbol() {
        return "XRP-USD";
    }

}
