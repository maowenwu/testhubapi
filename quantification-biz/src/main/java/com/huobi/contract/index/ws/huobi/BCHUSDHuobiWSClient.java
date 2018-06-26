package com.huobi.contract.index.ws.huobi;

public class BCHUSDHuobiWSClient extends AbstractHuobiWSClient {


    @Override
    public String getSubContent() {
        return "{\"sub\": \"market.bchbtc.trade.detail\", \"id\": \"id10\"}";
    }

    @Override
    public String getIndexSymbol() {
        return "BCH-USD";
    }


}
