package com.huobi.contract.index.ws.huobi;

public class XRPUSDHuobiWSClient extends AbstractHuobiWSClient {


    @Override
    public String getSubContent() {
        return "{\"sub\": \"market.xrpbtc.trade.detail\", \"id\": \"id10\"}";
    }

    @Override
    public String getIndexSymbol() {
        return "XRP-USD";
    }
}
