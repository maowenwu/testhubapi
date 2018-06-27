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
     * 获取OKEx合约深度信息
     *
     * @param symbol
     * @param contractType
     * @return
     */
    Object getOkDepth(String symbol, String contractType);

    /**
     * 获取OKEx合约交易记录信息
     *
     * @param symbol
     * @param contractType
     * @return
     */
    Object getOkTrades(String symbol, String contractType);

    /**
     * 获取OKEx合约指数信息
     *
     * @param symbol
     * @param contractType
     * @return
     */
    Object getOkIndex(String symbol, String contractType);


}
