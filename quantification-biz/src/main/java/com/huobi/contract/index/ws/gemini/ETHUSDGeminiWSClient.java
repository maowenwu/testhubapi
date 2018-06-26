package com.huobi.contract.index.ws.gemini;


import com.huobi.contract.index.ws.Constant;
import org.springframework.stereotype.Component;


public class ETHUSDGeminiWSClient extends AbstractGeminiWSClient {

    @Override
    public String getConnUrl() {
        return Constant.GEMINI_WSS + "ETHUSD";
    }

    @Override
    public String getIndexSymbol() {
        return "ETH-USD";
    }


}
