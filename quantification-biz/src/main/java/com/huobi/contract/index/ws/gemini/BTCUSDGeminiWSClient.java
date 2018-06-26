package com.huobi.contract.index.ws.gemini;


import com.huobi.contract.index.ws.Constant;
import org.springframework.stereotype.Component;


public class BTCUSDGeminiWSClient extends AbstractGeminiWSClient {


    @Override
    public String getConnUrl() {
        return Constant.GEMINI_WSS + "BTCUSD";
    }

    @Override
    public String getIndexSymbol() {
        return "BTC-USD";
    }


}
