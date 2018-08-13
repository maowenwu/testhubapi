package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class FutureAccountServiceImpl implements FutureAccountService {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult<FutureBalanceRespDto> getBalance(FutureBalanceReqDto reqDto) {
        try {
            FutureBalanceRespDto balanceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新资产
                    Map<String, QuanAccountFutureAsset> assetMap = redisService.getUserInfoFuture(reqDto.getExchangeId(), reqDto.getAccountId());
                    if (assetMap == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = new ArrayList<>(assetMap.values()).get(0).getUpdateTime();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        return parseBalance(reqDto.getCoinType(), assetMap);
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            return ServiceResult.buildSuccessResult(balanceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
    }

    private FutureBalanceRespDto parseBalance(String coinType, Map<String, QuanAccountFutureAsset> assetMap) {
        FutureBalanceRespDto respDto = new FutureBalanceRespDto();
        Map<String, FutureBalanceRespDto.DataBean> data = new ConcurrentHashMap<>();
        assetMap.forEach((k, v) -> {
            data.put(k, convertToDto(v));
        });
        if (StringUtils.isNotEmpty(coinType)) {
            data.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    data.remove(k);
                }
            });
        }
        respDto.setData(data);
        return respDto;
    }

    private FutureBalanceRespDto.DataBean convertToDto(QuanAccountFutureAsset futureAsset) {
        FutureBalanceRespDto.DataBean respDto = new FutureBalanceRespDto.DataBean();
        respDto.setMarginBalance(futureAsset.getMarginBalance());
        // 持仓保证金
        respDto.setMarginPosition(futureAsset.getMarginPosition());
        respDto.setMarginFrozen(futureAsset.getMarginFrozen());
        respDto.setMarginAvailable(futureAsset.getMarginAvailable());
        respDto.setProfitReal(futureAsset.getProfitReal());
        respDto.setProfitUnreal(futureAsset.getProfitUnreal());
        respDto.setRiskRate(futureAsset.getRiskRate());
        return respDto;
    }

    @Override
    public ServiceResult<FuturePositionRespDto> getPosition(FuturePositionReqDto reqDto) {
        try {
            FuturePositionRespDto positionRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新资产
                    List<QuanAccountFuturePosition> futurePositions = redisService.getPositionFuture(reqDto.getExchangeId(), reqDto.getAccountId());
                    if (futurePositions == null) {
                        ThreadUtils.sleep10();
                        continue;
                    } else if (futurePositions.isEmpty()) {
                        // 说明持仓为空
                        return new FuturePositionRespDto(null);
                    } else {
                        Date ts = futurePositions.get(0).getUpdateTime();
                        if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                            return parsePosition(reqDto.getCoinType(), futurePositions);
                        } else {
                            ThreadUtils.sleep10();
                            continue;
                        }
                    }
                }
                return null;
            }, reqDto.getTimeout());
            return ServiceResult.buildSuccessResult(positionRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
    }

    private FuturePositionRespDto parsePosition(String coinType, List<QuanAccountFuturePosition> futurePositions) {
        ConcurrentMap<String, List<QuanAccountFuturePosition>> dataMap = futurePositions.stream()
                .collect(Collectors.groupingByConcurrent((a) -> a.getBaseCoin().toLowerCase()));
        if (StringUtils.isNotEmpty(coinType)) {
            dataMap.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    dataMap.remove(k);
                }
            });
        }
        Map<String, List<FuturePositionRespDto.Position>> resultData = new HashMap<>();
        dataMap.forEach((k, v) -> {
            List<FuturePositionRespDto.Position> positionList = new ArrayList<>();
            v.stream().forEach(e -> {
                FuturePositionRespDto.Position posi = new FuturePositionRespDto.Position();
                BeanUtils.copyProperties(e, posi);
                positionList.add(posi);
            });
            resultData.put(k, positionList);
        });
        return new FuturePositionRespDto(resultData);
    }


}
