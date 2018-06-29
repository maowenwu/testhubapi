package com.huobi.quantification.service.market;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface MarketService {

    /**
     * 获取OKEx合约行情数据
     *
     * @param symbol
     * @param contractType
     * @return
     */
    Object getOkTicker(String symbol, String contractType);

    /**
     * 获取所提供的所有ticker数据，
     * 保存到数据库
     */
    void storeOkTicker();

    /**
     * 获取OKEx合约深度信息
     *
     * @param symbol
     * @param contractType
     * @return
     */
    Object getOkDepth(String symbol, String contractType);


    /**
     * 获取所提供的所有Depth数据，
     * 保存到数据库
     */
    void storeOkDepth();

    /**
     * 获取OKEx合约K线信息
     *
     * @param symbol       btc_usd ltc_usd eth_usd etc_usd bch_usd
     * @param type         1min/3min/5min/15min/30min/1day/3day/1week/1hour/2hour/4hour/6hour/12hour
     * @param contractType 合约类型: this_week:当周 next_week:下周 quarter:季度
     * @param size         指定获取数据的条数
     * @param since        时间戳（eg：1417536000000）。 返回该时间戳以后的数据
     * @return
     */
    Object getOkFutureKline(String symbol, String type, String contractType, int size, long since);


    /**
     * 获取所提供的所有kline数据，
     * 保存到数据库
     */
    void storeOkFutureKline();

    void getLatestOkFutureKline(String symbol, String type, String contractType);
}
