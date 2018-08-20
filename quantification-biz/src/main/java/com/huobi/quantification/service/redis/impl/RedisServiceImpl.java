package com.huobi.quantification.service.redis.impl;

import com.huobi.quantification.entity.*;
import com.huobi.quantification.service.redis.RedisService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedissonClient client;

    @Override
    public void saveAccountFuture(int exchangeId, Long accountId, Map<String, QuanAccountFutureAsset> assetMap) {
        RMap<Long, Map<String, QuanAccountFutureAsset>> map = client.getMap("quan:future:account:" + exchangeId);
        map.put(accountId, assetMap);
    }

    @Override
    public Map<String, QuanAccountFutureAsset> getAccountFuture(int exchangeId, Long accountId) {
        RMap<Long, Map<String, QuanAccountFutureAsset>> map = client.getMap("quan:future:account:" + exchangeId);
        return map.get(accountId);
    }

    @Override
    public void savePositionFuture(int exchangeId, Long accountId, List<QuanAccountFuturePosition> futurePositions) {
        RMap<Long, List<QuanAccountFuturePosition>> map = client.getMap("quan:future:position:" + exchangeId);
        map.put(accountId, futurePositions);
    }

    @Override
    public List<QuanAccountFuturePosition> getPositionFuture(int exchangeId, Long accountId) {
        RMap<Long, List<QuanAccountFuturePosition>> map = client.getMap("quan:future:position:" + exchangeId);
        return map.get(accountId);
    }


    @Override
    public void saveDepthFuture(int exchangeId, String symbol, String contractType, String depthType, List<QuanDepthFutureDetail> list) {
        RMap<String, List<QuanDepthFutureDetail>> map = client
                .getMap("quan:future:depth:" + exchangeId + ":" + symbol + ":" + depthType);
        map.put(contractType, list);
    }

    @Override
    public List<QuanDepthFutureDetail> getDepthFuture(int exchangeId, String symbol, String contractType, String depthType) {
        RMap<String, List<QuanDepthFutureDetail>> map = client
                .getMap("quan:future:depth:" + exchangeId + ":" + symbol + ":" + depthType);
        return map.get(contractType);
    }


    @Override
    public void saveAccountSpot(List<QuanAccountAsset> quanAssetList, int exchangeId, long accountId) {
        RMap<Long, List<QuanAccountAsset>> map = client.getMap("quan:spot:account:" + exchangeId);
        map.put(accountId, quanAssetList);
    }


    @Override
    public List<QuanAccountAsset> getAccountSpot(long accountId, int exchangeId) {
        RMap<Long, List<QuanAccountAsset>> map = client.getMap("quan:spot:account:" + exchangeId);
        return map.get(accountId);
    }

    @Override
    public void saveIndexFuture(QuanIndexFuture quanIndexFuture) {
        RMap<String, QuanIndexFuture> map = client
                .getMap("quan:future:index:" + quanIndexFuture.getExchangeId());
        map.put(quanIndexFuture.getSymbol(), quanIndexFuture);
    }

    @Override
    public QuanIndexFuture getIndexFuture(int exchangeId, String symbol) {
        RMap<String, QuanIndexFuture> map = client.getMap("quan:future:index:" + exchangeId);
        QuanIndexFuture indexFuture = map.get(symbol);
        if (indexFuture != null) {
            return indexFuture;
        } else {
            return null;
        }
    }

    @Override
    public void saveCurrentPriceFuture(int exId, String symbol, String contractType, QuanTradeFuture tradeFuture) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan:future:trade:" + exId + ":" + symbol);
        map.put(contractType, tradeFuture);
    }

    @Override
    public QuanTradeFuture getCurrentPriceFuture(int exId, String symbol, String contractType) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan:future:trade:" + exId + ":" + symbol);
        return map.get(contractType);
    }

    @Override
    public void saveCurrentPriceSpot(int exId, String symbol, QuanTrade trade) {
        RMap<String, QuanTrade> map = client.getMap("quan:spot:trade:" + exId);
        map.put(symbol, trade);
    }

    @Override
    public QuanTrade getCurrentPriceSpot(int exchangeId, String symbol) {
        RMap<String, QuanTrade> map = client.getMap("quan:spot:trade:" + exchangeId);
        return map.get(symbol);
    }

    @Override
    public void saveDepthSpot(int exchangeId, String symbol, List<QuanDepthDetail> list) {
        RMap<String, List<QuanDepthDetail>> map = client.getMap("quan:spot:depth:" + exchangeId);
        map.put(symbol, list);
    }

    @Override
    public List<QuanDepthDetail> getDepthSpot(int exchangeId, String symbol) {
        RMap<String, List<QuanDepthDetail>> map = client.getMap("quan:spot:depth:" + exchangeId);
        return map.get(symbol);
    }

}
