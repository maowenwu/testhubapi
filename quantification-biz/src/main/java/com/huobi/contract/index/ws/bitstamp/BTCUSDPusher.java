package com.huobi.contract.index.ws.bitstamp;

import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


public class BTCUSDPusher extends AbstractPusher implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        connect("live_trades");
        ConnectionStatusManager.register(ExchangeEnum.BITSTAMP, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }

    @Override
    protected String getIndexSymbol() {
        return "BTC-USD";
    }
}
