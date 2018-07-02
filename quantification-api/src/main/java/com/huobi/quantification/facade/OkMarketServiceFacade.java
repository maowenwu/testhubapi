package com.huobi.quantification.facade;

import com.huobi.quantification.common.ServiceResult;

public interface OkMarketServiceFacade {

    /**
     * 获取OKEx合约行情数据
     *
     * @param symbol
     * @param contractType
     * @return
     */
    ServiceResult getOkTicker(String symbol, String contractType);


    /**
     * 获取OKEx合约深度信息
     *
     * @param symbol
     * @param contractType
     * @return
     */
    ServiceResult getOkDepth(String symbol, String contractType);


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
    ServiceResult getOkKline(String symbol, String type, String contractType, int size, long since);
}