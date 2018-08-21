package com.huobi.quantification.api.future;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;

public interface FutureMarketService {


    /**
     * 获取最新成交价
     */
    ServiceResult<FutureCurrentPriceRespDto> getCurrentPrice(FutureCurrentPriceReqDto currentPriceReqDto);

    /**
     * 获取深度
     */
    ServiceResult<FutureDepthRespDto> getDepth(FutureDepthReqDto depthReqDto);


    /**
     * 获取最新指数价格
     */
    ServiceResult<FutureCurrentIndexRespDto> getCurrentIndexPrice(FutureCurrentIndexReqDto currentIndexReqDto);
}
