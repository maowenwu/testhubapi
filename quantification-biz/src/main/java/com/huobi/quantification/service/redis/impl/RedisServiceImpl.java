package com.huobi.quantification.service.redis.impl;

import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.service.redis.RedisService;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedissonClient client;

    @Override
    public void saveOkUserInfo(Long accountId, List<QuanAccountFutureAsset> list) {
        RMap<String, Object> map = client.getMap("quan.account.future.ok.userinfo." + accountId);
        for (QuanAccountFutureAsset asset : list) {
            map.put(asset.getSymbol(), asset);
        }
    }

    @Override
    public void saveOkPosition(Long accountId, String symbol, String type, List<QuanAccountFuturePosition> list) {
        RMap<String, Object> map = client.getMap("quan.account.future.ok.position." + accountId + "." + symbol);
        map.put(type, list);
    }

    @Override
    public void saveOkTicker(String symbol, String contractType, QuanTickerFuture ticker) {
        RMap<String, Object> map = client.getMap("quan.market.future.ok.ticker." + symbol);
        map.put(contractType, ticker);
    }

    @Override
    public void saveOkDepth(String symbol, String contractType, List<QuanDepthFutureDetail> list) {
        RMap<String, Object> map = client.getMap("quan.market.future.ok.depth." + symbol);
        map.put(contractType, list);
    }

    @Override
    public void saveOkKline(String symbol, String type, String contractType, List<QuanKlineFuture> redisKline) {
        RList<QuanKlineFuture> list = client.getList("quan.market.future.ok.kline." + symbol + "." + type + "." + contractType);
        for (QuanKlineFuture kline : redisKline) {
            list.add(kline);
        }
    }
}