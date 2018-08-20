package com.huobi.quantification.provider;

import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanTrade;
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
public class SpotMarketServiceImpl implements SpotMarketService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult<SpotCurrentPriceRespDto> getCurrentPrice(SpotCurrentPriceReqDto currentPriceReqDto) {
        ServiceResult<SpotCurrentPriceRespDto> serviceResult = null;
        try {
            SpotCurrentPriceRespDto currentPriceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新成交价格
                    QuanTrade quanTrade = redisService.getCurrentPriceSpot(currentPriceReqDto.getExchangeId(),
                            getSymbol(currentPriceReqDto.getExchangeId(), currentPriceReqDto.getBaseCoin(),
                                    currentPriceReqDto.getQuoteCoin()));
                    if (quanTrade == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = quanTrade.getTs();
                    if (DateUtils.withinMaxDelay(ts, currentPriceReqDto.getMaxDelay())) {
                        SpotCurrentPriceRespDto respDto = new SpotCurrentPriceRespDto();
                        respDto.setTs(ts);
                        respDto.setCurrentPrice(quanTrade.getPrice());
                        return respDto;
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, currentPriceReqDto.getTimeout());
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

    private String getSymbol(int exchangeId, String baseCoin, String quoteCoin) {
        if (exchangeId == ExchangeEnum.HUOBI.getExId() || exchangeId == ExchangeEnum.HUOBI_FUTURE.getExId()) {
            return baseCoin.toLowerCase() + "_" + quoteCoin.toLowerCase();
        } else {
            throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
        }
    }

    @Override
    public ServiceResult<SpotDepthRespDto> getDepth(SpotDepthReqDto depthReqDto) {
        try {
            SpotDepthRespDto currentPriceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新深度
                    List<QuanDepthDetail> huobiDepths = redisService.getDepthSpot(depthReqDto.getExchangeId(),
                            getSymbol(depthReqDto.getExchangeId(), depthReqDto.getBaseCoin(), depthReqDto.getQuoteCoin()), depthReqDto.getDepthType());

                    if (CollectionUtils.isEmpty(huobiDepths)) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = huobiDepths.get(0).getDateUpdate();
                    if (DateUtils.withinMaxDelay(ts, depthReqDto.getMaxDelay())) {
                        SpotDepthRespDto respDto = new SpotDepthRespDto();
                        respDto.setTs(ts);
                        respDto.setData(convertDepthToDto(huobiDepths));
                        return respDto;
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, depthReqDto.getTimeout());
            return ServiceResult.buildSuccessResult(currentPriceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
    }

    private SpotDepthRespDto.DataBean convertDepthToDto(List<QuanDepthDetail> depthSpot) {
        SpotDepthRespDto.DataBean dataBean = new SpotDepthRespDto.DataBean();
        List<SpotDepthRespDto.Depth> asks = new ArrayList<>();
        List<SpotDepthRespDto.Depth> bids = new ArrayList<>();
        dataBean.setAsks(asks);
        dataBean.setBids(bids);
        for (QuanDepthDetail depthDetail : depthSpot) {
            if (depthDetail.getDetailType().equals(DepthEnum.ASKS.getIntType())) {
                SpotDepthRespDto.Depth depth = new SpotDepthRespDto.Depth();
                depth.setAmount(depthDetail.getDetailAmount());
                depth.setPrice(depthDetail.getDetailPrice());
                asks.add(depth);
            } else {
                SpotDepthRespDto.Depth depth = new SpotDepthRespDto.Depth();
                depth.setAmount(depthDetail.getDetailAmount());
                depth.setPrice(depthDetail.getDetailPrice());
                bids.add(depth);
            }
        }
        return dataBean;
    }

}
