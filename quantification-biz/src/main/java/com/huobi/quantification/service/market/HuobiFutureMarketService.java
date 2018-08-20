package com.huobi.quantification.service.market;

import com.huobi.quantification.response.future.HuobiFutureDepthResponse;
import com.huobi.quantification.response.future.HuobiFutureKlineResponse;
import com.huobi.quantification.response.future.HuobiFutureTickerResponse;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface HuobiFutureMarketService {

    /**
     * @param symbol       btc
     * @param contractType cw
     * @return
     */
    HuobiFutureTickerResponse queryTickerByAPI(String symbol, String contractType);

    /**
     * @param symbol       btc
     * @param contractType cw
     * @param period       1min
     * @param size         200
     * @return
     */
    HuobiFutureKlineResponse queryKlineByAPI(String symbol, String contractType, String period, int size);

    /**
     * @param symbol       btc
     * @param contractType cw
     * @param type         step5
     * @return
     */
    HuobiFutureDepthResponse queryDepthByAPI(String symbol, String contractType, String type);

    void updateHuobiCurrentPrice(String symbol, String contractType);

    void updateHuobiDepth(String symbol, String contractType, String depthType);



}
