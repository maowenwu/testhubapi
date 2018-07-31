package com.huobi.quantification.service.account.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.huobi.quantification.enums.ExchangeEnum;
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
        String body = httpService.doPost(HttpConstant.HUOBI_FUTURE_ACCOUNTINFO, params);
        return body;
    }

    @Override
    public String queryPositionByAPI(Long accountId) {
        HashMap<String, String> params = new HashMap<>();
        String body = httpService.doPost(HttpConstant.HUOBI_FUTURE_POSITION, params);
        return body;
    }

    @Override
    public void updateHuobiUserInfo(Long accountId) {
        Stopwatch started = Stopwatch.createStarted();
        Date now = new Date();
        logger.info("[HuobiUserInfo][accountId={}]任务开始", accountId);
        long queryId = System.currentTimeMillis();
        String body = queryUserInfoByAPI(accountId);
        QuanAccountFutureAsset futureAsset = new QuanAccountFutureAsset();
        futureAsset.setAccountSourceId(accountId);
        futureAsset.setQueryId(queryId);
        futureAsset.setRespBody(body);
        futureAsset.setCreateTime(now);
        futureAsset.setUpdateTime(now);

        redisService.saveFutureUserInfo(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, futureAsset);
        logger.info("[HuobiUserInfo][accountId={}]任务结束，耗时：" + started, accountId);
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
        redisService.saveFuturePosition(ExchangeEnum.HUOBI_FUTURE.getExId(), accountId, position);
        logger.info("[HuobiPosition][accountId={},]任务结束，耗时：" + started, accountId);
    }

	@Override
	public void saveFutureAccountsInfo(List<Long> accountIds) {
		for (Long accountId : accountIds) {
			String queryUserInfoByAPI = queryUserInfoByAPI(accountId);
			String queryPositionByAPI = queryPositionByAPI(accountId);
		}
	}
}
