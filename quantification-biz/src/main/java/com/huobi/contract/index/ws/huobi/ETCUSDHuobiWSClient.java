package com.huobi.contract.index.ws.huobi;

public class ETCUSDHuobiWSClient extends AbstractHuobiWSClient {


    @Override
    public String getSubContent() {
        return "{\"sub\": \"market.etcbtc.trade.detail\", \"id\": \"id10\"}";
    }

    @Override
    public String getIndexSymbol() {
        return "ETC-USD";
    }
}
