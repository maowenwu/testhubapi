package com.huobi.quantification.service.account.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.dao.QuanAccountFutureSecretMapper;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.future.OKFuturePositionResponse;
import com.huobi.quantification.service.account.OkFutureAccountService;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@DependsOn("httpServiceImpl")
@Service
@Transactional
public class OkFutureAccountServiceImpl implements OkFutureAccountService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpService httpService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Resource
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Resource
    private QuanAccountFutureSecretMapper quanAccountFutureSecretMapper;


    public void updateOkUserInfo(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        Date now = new Date();
        logger.info("[OkUserInfo][accountId={}]任务开始", accountId);
        long queryId = System.currentTimeMillis();
        String body = queryOkUserInfoByAPI(accountId);
        QuanAccountFutureAsset futureAsset = new QuanAccountFutureAsset();
        futureAsset.setAccountSourceId(accountId);
        futureAsset.setQueryId(queryId);
        futureAsset.setRespBody(body);
        futureAsset.setCreateTime(now);
        futureAsset.setUpdateTime(now);
         /* for (QuanAccountFutureAsset asset : list) {
            asset.setQueryId(queryId);
            asset.setAccountSourceId(accountId);
            quanAccountFutureAssetMapper.insert(asset);
        }*/
        redisService.saveFutureUserInfo(ExchangeEnum.OKEX.getExId(), accountId, futureAsset);
        logger.info("[OkUserInfo][accountId={}]任务结束，耗时：" + started, accountId);
    }

    private String queryOkUserInfoByAPI(Long accountId) {
        Map<String, String> params = new HashMap<>();
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_USER_INFO, params);
        return body;
    }


    public void updateOkPosition(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        Date now = new Date();
        logger.info("[OkPosition][accountId={}]任务开始", accountId);
        long queryId = System.currentTimeMillis();
        OKFuturePositionResponse positionResponse = queryAllOkPositionByAPI(accountId);
        QuanAccountFuturePosition position = new QuanAccountFuturePosition();
        position.setAccountSourceId(accountId);
        position.setQueryId(queryId);
        position.setRespBody(JSON.toJSONString(positionResponse));
        position.setCreateTime(now);
        position.setUpdateTime(now);
        redisService.saveFuturePosition(ExchangeEnum.OKEX.getExId(), accountId, position);
        logger.info("[OkPosition][accountId={},]任务结束，耗时：" + started, accountId);
    }

    private OKFuturePositionResponse queryAllOkPositionByAPI(Long accountId) {
        OKFuturePositionResponse response = new OKFuturePositionResponse();
        response.setHolding(new ArrayList<>());
        response.setResult(true);
        OKFuturePositionResponse btcUsd = queryOkPositionByAPI(accountId, "btc_usd");
        if (btcUsd.isResult()) {
            response.setForceLiquPrice(btcUsd.getForceLiquPrice());
            response.getHolding().addAll(btcUsd.getHolding());
        }
        OKFuturePositionResponse ltcUsd = queryOkPositionByAPI(accountId, "ltc_usd");
        if (ltcUsd.isResult()) {
            response.setForceLiquPrice(ltcUsd.getForceLiquPrice());
            response.getHolding().addAll(ltcUsd.getHolding());
        }
        OKFuturePositionResponse ethUsd = queryOkPositionByAPI(accountId, "eth_usd");
        if (ethUsd.isResult()) {
            response.setForceLiquPrice(ethUsd.getForceLiquPrice());
            response.getHolding().addAll(ethUsd.getHolding());
        }
        OKFuturePositionResponse etcUsd = queryOkPositionByAPI(accountId, "etc_usd");
        if (etcUsd.isResult()) {
            response.setForceLiquPrice(etcUsd.getForceLiquPrice());
            response.getHolding().addAll(etcUsd.getHolding());
        }
        OKFuturePositionResponse bchUsd = queryOkPositionByAPI(accountId, "bch_usd");
        if (bchUsd.isResult()) {
            response.setForceLiquPrice(bchUsd.getForceLiquPrice());
            response.getHolding().addAll(bchUsd.getHolding());
        }
        return response;
    }


    public OKFuturePositionResponse queryOkPositionByAPI(Long accountId, String symbol) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        //params.put("contract_type", contractType);
        String body = httpService.doOkSignedPost(accountId, HttpConstant.OK_POSITION, params);
        return JSON.parseObject(body, OKFuturePositionResponse.class);
    }


    @Override
    public List<Long> findAccountFutureByExchangeId(int exchangeId) {
        List<Long> list = quanAccountFutureMapper.selectByExchangeId(exchangeId);
        return list;
    }

    @Override
    public List<QuanAccountFutureSecret> findAccountFutureSecretById(Long id) {
        List<QuanAccountFutureSecret> list = quanAccountFutureSecretMapper.selectBySourceId(id);
        return list;
    }
}
