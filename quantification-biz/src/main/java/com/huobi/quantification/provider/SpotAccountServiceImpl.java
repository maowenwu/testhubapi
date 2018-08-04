package com.huobi.quantification.provider;

import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class SpotAccountServiceImpl implements SpotAccountService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult<SpotBalanceRespDto> getBalance(SpotBalanceReqDto reqDto) {
        ServiceResult<SpotBalanceRespDto> serviceResult = null;
        try {
            SpotBalanceRespDto balanceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新用户资产
                    List<QuanAccountAsset> assets = redisService.getAccountSpot(reqDto.getAccountId(),
                            reqDto.getExchangeId());
                    if (CollectionUtils.isEmpty(assets)) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = assets.get(0).getUpdateTime();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        return parseBalance(reqDto.getCoinType(), assets);
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(balanceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    private SpotBalanceRespDto parseBalance(String coinType, List<QuanAccountAsset> assets) {
        SpotBalanceRespDto respDto = new SpotBalanceRespDto();
        Map<String, SpotBalanceRespDto.DataBean> dataMap = new ConcurrentHashMap<>();
        for (QuanAccountAsset quanAccountAsset : assets) {
            SpotBalanceRespDto.DataBean dataBean = new SpotBalanceRespDto.DataBean();
            dataBean.setAvailable(quanAccountAsset.getAvailable());
            dataBean.setFrozen(quanAccountAsset.getFrozen());
            dataBean.setTotal(quanAccountAsset.getTotal());
            dataMap.put(quanAccountAsset.getCoinType(), dataBean);
        }
        if (StringUtils.isNotEmpty(coinType)) {
            dataMap.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    dataMap.remove(k);
                }
            });
        }
        respDto.setData(dataMap);
        return respDto;
    }


}
