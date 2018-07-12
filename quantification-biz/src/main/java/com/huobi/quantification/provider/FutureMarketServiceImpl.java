package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class FutureMarketServiceImpl implements FutureMarketService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult<FutureCurrentPriceRespDto> getCurrentPrice(FutureCurrentPriceReqDto reqDto) {
        ServiceResult<FutureCurrentPriceRespDto> serviceResult = new ServiceResult<>();
        try {
            FutureCurrentPriceRespDto currentPriceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新价格
                    QuanTradeFuture tradeFuture = redisService.getCurrentPrice(reqDto.getExchangeId(), getSymbol(reqDto.getExchangeId()
                            , reqDto.getBaseCoin(), reqDto.getQuoteCoin()), getContractType(reqDto.getExchangeId(), reqDto.getContractType()));
                    if (tradeFuture == null) {
                        continue;
                    }
                    Date ts = tradeFuture.getUpdateTime();
                    System.out.println("currentPriceRespDto时间：" + DateUtils.format(ts, "yyyy-MM-dd HH:mm:ss"));
                    System.out.println("当前时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        FutureCurrentPriceRespDto respDto = new FutureCurrentPriceRespDto();
                        respDto.setTs(ts);
                        respDto.setCurrentPrice(tradeFuture.getPrice());
                        return respDto;
                    } else {
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
            serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
            serviceResult.setData(currentPriceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult.setCode(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.TIMEOUT_ERROR.getMessage());
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<FutureDepthRespDto> getDepth(FutureDepthReqDto reqDto) {
        ServiceResult<FutureDepthRespDto> serviceResult = new ServiceResult<>();
        try {
            FutureDepthRespDto depthRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新K线
                    List<QuanDepthFutureDetail> depthFuture = redisService.getDepthFuture(reqDto.getExchangeId(), getSymbol(reqDto.getExchangeId()
                            , reqDto.getBaseCoin(), reqDto.getQuoteCoin()), getContractType(reqDto.getExchangeId(), reqDto.getContractType()));
                    if (CollectionUtils.isEmpty(depthFuture)) {
                        continue;
                    }
                    Date ts = depthFuture.get(0).getDateUpdate();
                    System.out.println("depth时间：" + DateUtils.format(ts, "yyyy-MM-dd HH:mm:ss"));
                    System.out.println("当前时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        FutureDepthRespDto respDto = new FutureDepthRespDto();
                        respDto.setTs(ts);
                        respDto.setData(convertDepthToDto(depthFuture));
                        return respDto;
                    } else {
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
            serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
            serviceResult.setData(depthRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult.setCode(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.TIMEOUT_ERROR.getMessage());
        }
        return serviceResult;
    }

    private FutureDepthRespDto.DataBean convertDepthToDto(List<QuanDepthFutureDetail> depthFuture) {
        FutureDepthRespDto.DataBean dataBean = new FutureDepthRespDto.DataBean();
        List<FutureDepthRespDto.Depth> asks = new ArrayList<>();
        List<FutureDepthRespDto.Depth> bids = new ArrayList<>();
        dataBean.setAsks(asks);
        dataBean.setBids(bids);
        for (QuanDepthFutureDetail depthFutureDetail : depthFuture) {
            if (depthFutureDetail.getDetailType().equals(DepthEnum.ASKS.getIntType())) {
                FutureDepthRespDto.Depth depth = new FutureDepthRespDto.Depth();
                depth.setAmount(depthFutureDetail.getDetailAmount());
                depth.setPrice(depthFutureDetail.getDetailPrice());
                asks.add(depth);
            } else {
                FutureDepthRespDto.Depth depth = new FutureDepthRespDto.Depth();
                depth.setAmount(depthFutureDetail.getDetailAmount());
                depth.setPrice(depthFutureDetail.getDetailPrice());
                bids.add(depth);
            }
        }
        return dataBean;
    }

    @Override
    public ServiceResult<FutureKlineRespDto> getKline(FutureKlineReqDto reqDto) {
        ServiceResult<FutureKlineRespDto> serviceResult = new ServiceResult<>();
        try {
            FutureKlineRespDto klineRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新K线
                    List<QuanKlineFuture> klineFutures = redisService.getKlineFuture(reqDto.getExchangeId(),
                            getSymbol(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getQuoteCoin()),
                            reqDto.getPeriod(), getContractType(reqDto.getExchangeId(), reqDto.getContractType()));
                    if (CollectionUtils.isEmpty(klineFutures)) {
                        continue;
                    }
                    Date ts = klineFutures.get(klineFutures.size() - 1).getTs();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        FutureKlineRespDto respDto = new FutureKlineRespDto();
                        respDto.setTs(ts);
                        respDto.setData(convertToDto(klineFutures));
                        return respDto;
                    } else {
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
            serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
            serviceResult.setData(klineRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult.setCode(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.TIMEOUT_ERROR.getMessage());
        }
        return serviceResult;
    }

    private List<FutureKlineRespDto.DataBean> convertToDto(List<QuanKlineFuture> klineFutures) {
        List<FutureKlineRespDto.DataBean> dataBeans = new ArrayList<>();
        for (QuanKlineFuture future : klineFutures) {
            FutureKlineRespDto.DataBean bean = new FutureKlineRespDto.DataBean();
            bean.setId(future.getId());
            bean.setAmount(future.getAmount());
            bean.setOpen(future.getOpen());
            bean.setClose(future.getClose());
            bean.setLow(future.getLow());
            bean.setHigh(future.getHigh());
            dataBeans.add(bean);
        }
        return dataBeans;
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

    private String getContractType(int exchangeId, String contractType) {
        if (exchangeId == ExchangeEnum.OKEX.getExId()) {
            return contractType;
        } else {
            throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
        }
    }
}
