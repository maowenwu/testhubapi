package com.huobi.contract.index.ws.gdax;

public class BTCUSDGdaxWSClient extends AbstractGdaxWSClient {

    @Override
    protected void handleOpen() {
        logger.info("gdax 发送订阅");
        send("{\"type\": \"subscribe\",\"channels\": [{\"name\": \"matches\",\"product_ids\": [\"BTC-USD\"]}]}");
    }


    @Override
    protected String getIndexSymbol() {
        return "BTC-USD";
    }

}
