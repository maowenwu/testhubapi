package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;
import org.springframework.stereotype.Service;


@Service
public class FutureMarketServiceImpl implements FutureMarketService {

    @Override
    public ServiceResult<FutureCurrentPriceRespDto> getCurrentPrice(FutureCurrentPriceReqDto currentPriceReqDto) {
        return null;
    }

    @Override
    public ServiceResult<FutureDepthRespDto> getDepth(FutureDepthReqDto depthReqDto) {
        return null;
    }

    @Override
    public ServiceResult<FutureKlineRespDto> getKline(FutureKlineReqDto klineReqDto) {
        return null;
    }

    @Override
    public ServiceResult<FutureCurrentIndexRespDto> getCurrentIndexPrice(FutureCurrentIndexReqDto currentIndexReqDto) {
        return null;
    }
}
