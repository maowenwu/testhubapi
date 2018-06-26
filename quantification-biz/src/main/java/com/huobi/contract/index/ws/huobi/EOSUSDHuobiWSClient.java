package com.huobi.contract.index.ws.huobi;

public class EOSUSDHuobiWSClient extends AbstractHuobiWSClient  {



    @Override
    public String getSubContent() {
        return  "{\"sub\": \"market.eosbtc.trade.detail\", \"id\": \"id10\"}";
    }

    @Override
    public String getIndexSymbol() {
        return "EOS-USD";
    }
}
