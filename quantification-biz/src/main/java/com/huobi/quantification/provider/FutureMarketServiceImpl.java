package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class FutureMarketServiceImpl implements FutureMarketService {

    @Autowired
    private RedisService redisService;

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
    public ServiceResult<FutureCurrentIndexRespDto> getCurrentIndexPrice(FutureCurrentIndexReqDto reqDto) {
        ServiceResult<FutureCurrentIndexRespDto> serviceResult = new ServiceResult<>();
        try {
            FutureCurrentIndexRespDto indexRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    QuanIndexFuture futureIndex = redisService.getIndexFuture(reqDto.getExchangeId(), getSymbol(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getQuoteCoin()));
                    if (futureIndex == null) {
                        continue;
                    }
                    if (DateUtils.withinMaxDelay(futureIndex.getUpdateTime(), reqDto.getMaxDelay())) {
                        FutureCurrentIndexRespDto respDto = new FutureCurrentIndexRespDto();
                        respDto.setTs(futureIndex.getUpdateTime().getTime());
                        respDto.setCurrentIndexPrice(futureIndex.getFutureIndex());
                        return respDto;
                    } else {
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
            serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
            serviceResult.setData(indexRespDto);
        } catch (ExecutionException e) {
            serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
        } catch (TimeoutException e) {
            serviceResult.setCode(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.TIMEOUT_ERROR.getMessage());
        }
        return serviceResult;
    }

    private String getSymbol(int exchangeId, String baseCoin, String quoteCoin) {
        if (exchangeId == ExchangeEnum.OKEX.getExId()) {
            return baseCoin.toLowerCase() + "_" + quoteCoin.toLowerCase();
        } else {
            throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
        }
    }
}
