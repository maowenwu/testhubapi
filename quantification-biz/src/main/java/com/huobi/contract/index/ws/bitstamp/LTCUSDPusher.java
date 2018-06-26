package com.huobi.contract.index.ws.bitstamp;

import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;



public class LTCUSDPusher extends AbstractPusher implements InitializingBean {

    @Override
    protected String getIndexSymbol() {
        return "LTC-USD";
    }

    @Override
    public void afterPropertiesSet() {
        connect("live_trades_ltcusd");
        ConnectionStatusManager.register(ExchangeEnum.BITSTAMP, getIndexSymbol(), this);
        logger.info("websocket connect success");
    }
}
