package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.enums.DepthEnum;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
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

    @Autowired
    private ContractService contractService;

    @Override
    public ServiceResult<FutureCurrentPriceRespDto> getCurrentPrice(FutureCurrentPriceReqDto reqDto) {
        ServiceResult<FutureCurrentPriceRespDto> serviceResult = null;
        try {
            FutureCurrentPriceRespDto currentPriceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新价格
                    QuanTradeFuture tradeFuture = redisService.getCurrentPriceFuture(reqDto.getExchangeId(),
                            getSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin()), reqDto.getContractType());
                    if (tradeFuture == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = tradeFuture.getUpdateTime();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        FutureCurrentPriceRespDto respDto = new FutureCurrentPriceRespDto();
                        respDto.setTs(ts);
                        respDto.setCurrentPrice(tradeFuture.getPrice());
                        return respDto;
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(currentPriceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<FutureDepthRespDto> getDepth(FutureDepthReqDto reqDto) {
        ServiceResult<FutureDepthRespDto> serviceResult = null;
        try {
            FutureDepthRespDto depthRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    List<QuanDepthFutureDetail> depthFuture = redisService.getDepthFuture(reqDto.getExchangeId(),
                            getSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin()), reqDto.getContractType());
                    if (depthFuture == null) {
                        // 任务没有启动
                        ThreadUtils.sleep10();
                        continue;
                    } else if (depthFuture.isEmpty()) {
                        // 代表没有深度
                        return new FutureDepthRespDto();
                    } else {
                        Date ts = depthFuture.get(0).getDateUpdate();
                        if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                            FutureDepthRespDto respDto = new FutureDepthRespDto();
                            respDto.setTs(ts);
                            respDto.setData(convertDepthToDto(depthFuture));
                            return respDto;
                        } else {
                            ThreadUtils.sleep10();
                            continue;
                        }
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(depthRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
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
        ServiceResult<FutureKlineRespDto> serviceResult = null;
        try {
            FutureKlineRespDto klineRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新K线
                    List<QuanKlineFuture> klineFutures = redisService.getKlineFuture(reqDto.getExchangeId(), getSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin()),
                            reqDto.getPeriod(), reqDto.getContractType());
                    if (CollectionUtils.isEmpty(klineFutures)) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = klineFutures.get(klineFutures.size() - 1).getTs();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        FutureKlineRespDto respDto = new FutureKlineRespDto();
                        respDto.setTs(ts);
                        respDto.setData(convertToDto(klineFutures, reqDto.getSize()));
                        return respDto;
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(klineRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    private List<FutureKlineRespDto.DataBean> convertToDto(List<QuanKlineFuture> klineFutures, int size) {
        List<FutureKlineRespDto.DataBean> dataBeans = new ArrayList<>();
        for (int i = 0; i < klineFutures.size(); i++) {
            QuanKlineFuture future = klineFutures.get(i);
            FutureKlineRespDto.DataBean bean = new FutureKlineRespDto.DataBean();
            bean.setId(future.getId());
            bean.setAmount(future.getAmount());
            bean.setOpen(future.getOpen());
            bean.setClose(future.getClose());
            bean.setLow(future.getLow());
            bean.setHigh(future.getHigh());
            dataBeans.add(bean);
            if (i + 1 >= size) {
                break;
            }
        }
        return dataBeans;
    }

    @Override
    public ServiceResult<FutureCurrentIndexRespDto> getCurrentIndexPrice(FutureCurrentIndexReqDto reqDto) {
        ServiceResult<FutureCurrentIndexRespDto> serviceResult = null;
        try {
            FutureCurrentIndexRespDto indexRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    QuanIndexFuture futureIndex = redisService.getIndexFuture(reqDto.getExchangeId(), getSymbol(reqDto.getBaseCoin(), reqDto.getQuoteCoin()));
                    if (futureIndex == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    if (DateUtils.withinMaxDelay(futureIndex.getUpdateTime(), reqDto.getMaxDelay())) {
                        FutureCurrentIndexRespDto respDto = new FutureCurrentIndexRespDto();
                        respDto.setTs(futureIndex.getUpdateTime().getTime());
                        respDto.setCurrentIndexPrice(futureIndex.getFutureIndex());
                        return respDto;
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(indexRespDto);
        } catch (ExecutionException e) {
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    private String getSymbol(String baseCoin, String quoteCoin) {
        return baseCoin.toLowerCase() + "_" + quoteCoin.toLowerCase();
    }


}
