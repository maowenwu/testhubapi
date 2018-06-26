package com.huobi.contract.index.ws.gdax;

public class LTCUSDGdaxWSClient extends AbstractGdaxWSClient {

    @Override
    protected void handleOpen() {
        logger.info("gdax 发送订阅");
        send("{\"type\": \"subscribe\",\"channels\": [{\"name\": \"matches\",\"product_ids\": [\"LTC-USD\"]}]}");
    }


    @Override
    protected String getIndexSymbol() {
        return "LTC-USD";
    }

}
