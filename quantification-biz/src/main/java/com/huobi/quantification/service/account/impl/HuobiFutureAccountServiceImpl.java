package com.huobi.quantification.service.account.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.common.util.StorageSupport;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.future.HuobiFutureUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.service.account.HuobiFutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;

@DependsOn("httpServiceImpl")
@Service
@Transactional
public class HuobiFutureAccountServiceImpl implements HuobiFutureAccountService {

    @Autowired
    private HttpService httpService;

    @Autowired
    private RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String queryUserInfoByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("symbol", "BTC");
        params.put("userId", "156138");
        String body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_ACCOUNTINFO, params);
        return body;
    }

    @Override
    public String queryPositionByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "156138");
        String body = httpService.doPostJson(HttpConstant.HUOBI_FUTURE_POSITION, params);
        return body;
    }

    @Autowired
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Override
    public void updateHuobiUserInfo(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiUserInfo][accountId={}]任务开始", accountId);
        Long accountFutureId = quanAccountFutureMapper.selectAccountFutureId(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId);
        if (accountFutureId == null) {
            logger.error("更新火币期货账户信息失败，不存在该账户：accountSourceId={}", accountId);
            return;
        }
        long queryId = System.currentTimeMillis();
        String body = queryUserInfoByAPI(accountId);
        Map<String, QuanAccountFutureAsset> assetMap = parseHuobiFutureBalance(body);
        boolean isSave = StorageSupport.checkSavepoint("updateHuobiUserInfo");
        assetMap.forEach((k, v) -> {
            v.setCoinType(k);
            v.setQueryId(queryId);
            v.setAccountFutureId(accountFutureId);
            if (isSave) {
                quanAccountFutureAssetMapper.insert(v);
            }
        });
        redisService.saveUserInfoFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, assetMap);
        logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountId);
    }

    private Map<String, QuanAccountFutureAsset> parseHuobiFutureBalance(String body) {
        HuobiFutureUserInfoResponse response = JSON.parseObject(body, HuobiFutureUserInfoResponse.class);
        Map<String, QuanAccountFutureAsset> data = new ConcurrentHashMap<>();
        List<HuobiFutureUserInfoResponse.DataBean> dataBeans = response.getData();
        for (HuobiFutureUserInfoResponse.DataBean dataBean : dataBeans) {
            data.put(dataBean.getSymbol().toLowerCase(), convertToDto(dataBean));
        }
        return data;
    }

    private QuanAccountFutureAsset convertToDto(HuobiFutureUserInfoResponse.DataBean dataBean) {
        QuanAccountFutureAsset futureAsset = new QuanAccountFutureAsset();
        futureAsset.setMarginBalance(dataBean.getMarginBalance());
        // 持仓保证金
        futureAsset.setMarginPosition(dataBean.getMarginPosition());
        futureAsset.setMarginFrozen(dataBean.getMarginFrozen());
        futureAsset.setMarginAvailable(dataBean.getMarginAvailable());
        futureAsset.setProfitReal(dataBean.getProfitReal());
        futureAsset.setProfitUnreal(dataBean.getProfitUnreal());
        futureAsset.setRiskRate(dataBean.getRiskRate());
        futureAsset.setInit(0);
        futureAsset.setCreateTime(new Date());
        futureAsset.setUpdateTime(new Date());
        return futureAsset;
    }

    @Override
    public void updateHuobiPosition(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        Date now = new Date();
        logger.info("[HuobiPosition][accountId={}]任务开始", accountId);
        long queryId = System.currentTimeMillis();
        String body = queryPositionByAPI(accountId);
        QuanAccountFuturePosition position = new QuanAccountFuturePosition();
        position.setAccountSourceId(accountId);
        position.setQueryId(queryId);
        position.setRespBody(body);
        position.setCreateTime(now);
        position.setUpdateTime(now);
        redisService.savePositionFuture(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, position);
        logger.info("[HuobiPosition][accountId={},]任务结束，耗时：" + started, accountId);
    }

}
