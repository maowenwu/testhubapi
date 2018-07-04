package com.huobi.quantification.service.market;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface MarketService {


    void updateOkTicker(String symbol, String contractType);

    void updateOkDepth(String symbol, String contractType);

    /**
     * 获取所提供的所有kline数据，保存到数据库
     */
    void updateOkFutureKline(String symbol, String type, String contractType);
}
