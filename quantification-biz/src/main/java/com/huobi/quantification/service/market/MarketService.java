package com.huobi.quantification.service.market;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface MarketService {

    /**
     * 获取所提供的所有ticker数据，保存到数据库
     */
    void storeOkTicker();

    /**
     * 获取所提供的所有Depth数据，保存到数据库
     */
    void storeOkDepth();

    /**
     * 获取所提供的所有kline数据，保存到数据库
     */
    void storeOkFutureKline();

    void getLatestOkFutureKline(String symbol, String type, String contractType);
}
