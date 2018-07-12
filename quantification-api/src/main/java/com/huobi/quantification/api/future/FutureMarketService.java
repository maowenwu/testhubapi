package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;

public interface FutureMarketService {


    /**
     * 获取最新成交价
     *
     * @param currentPriceReqDto
     * @return
     */
    ServiceResult<FutureCurrentPriceRespDto> getCurrentPrice(FutureCurrentPriceReqDto currentPriceReqDto);

    /**
     * 获取深度
     *
     * @param depthReqDto
     * @return
     */
    ServiceResult<FutureDepthRespDto> getDepth(FutureDepthReqDto depthReqDto);


    /**
     * 获取K线信息
     *
     * @param klineReqDto
     * @return
     */
    ServiceResult<FutureKlineRespDto> getKline(FutureKlineReqDto klineReqDto);

    /**
     * 获取最新指数价格
     *
     * @param currentIndexReqDto
     * @return
     */
    ServiceResult<FutureCurrentIndexRespDto> getCurrentIndexPrice(FutureCurrentIndexReqDto currentIndexReqDto);
}
