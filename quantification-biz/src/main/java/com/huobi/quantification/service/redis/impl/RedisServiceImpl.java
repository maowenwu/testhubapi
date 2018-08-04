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
    public void saveUserInfoFuture(int exchangeId, Long accountId, Map<String, QuanAccountFutureAsset> assetMap) {
        RMap<Long, Map<String, QuanAccountFutureAsset>> map = client.getMap("quan.account.future.userinfo." + exchangeId);
        map.put(accountId, assetMap);
    }

    @Override
    public  Map<String, QuanAccountFutureAsset> getUserInfoFuture(int exchangeId, Long accountId) {
        RMap<Long,  Map<String, QuanAccountFutureAsset>> map = client.getMap("quan.account.future.userinfo." + exchangeId);
        return map.get(accountId);
    }

    @Override
    public void savePositionFuture(int exchangeId, Long accountId, QuanAccountFuturePosition position) {
        RMap<Long, QuanAccountFuturePosition> map = client.getMap("quan.account.future.position." + exchangeId);
        map.put(accountId, position);
    }

    @Override
    public QuanAccountFuturePosition getPositionFuture(int exchangeId, Long accountId) {
        RMap<Long, QuanAccountFuturePosition> map = client.getMap("quan.account.future.position." + exchangeId);
        return map.get(accountId);
    }



    @Override
    public void saveDepthFuture(int exchangeId, String symbol, String contractType, List<QuanDepthFutureDetail> list) {
        RMap<String, List<QuanDepthFutureDetail>> map = client
                .getMap("quan.market.future.depth." + exchangeId + "." + symbol);
        map.put(contractType, list);
    }

    @Override
    public List<QuanDepthFutureDetail> getDepthFuture(int exchangeId, String symbol, String contractType) {
        RMap<String, List<QuanDepthFutureDetail>> map = client
                .getMap("quan.market.future.depth." + exchangeId + "." + symbol);
        return map.get(contractType);
    }

    @Override
    public void saveKlineFuture(int exchangeId, String symbol, String type, String contractType,
                                List<QuanKlineFuture> redisKline) {
        RMap<String, List<QuanKlineFuture>> map = client
                .getMap("quan.market.future.kline." + exchangeId + "." + type + "." + symbol);
        map.put(contractType, redisKline);
    }

    @Override
    public List<QuanKlineFuture> getKlineFuture(int exchangeId, String symbol, String type, String contractType) {
        RMap<String, List<QuanKlineFuture>> map = client
                .getMap("quan.market.future.kline." + exchangeId + "." + type + "." + symbol);
        return map.get(contractType);
    }


    @Override
    public void saveAccountSpot(List<QuanAccountAsset> quanAssetList, int exchangId, long accountId) {
        RMap<Long, List<QuanAccountAsset>> map = client.getMap("quan.account.spot.accountasset." + exchangId);
        map.put(accountId, quanAssetList);
    }



    @Override
    public void saveIndexFuture(QuanIndexFuture quanIndexFuture) {
        RMap<String, QuanIndexFuture> map = client
                .getMap("quan.market.future.index." + quanIndexFuture.getExchangeId());
        map.put(quanIndexFuture.getSymbol(), quanIndexFuture);
    }

    @Override
    public QuanIndexFuture getIndexFuture(int exchangeId, String symbol) {
        RMap<String, QuanIndexFuture> map = client.getMap("quan.market.future.index." + exchangeId);
        QuanIndexFuture indexFuture = map.get(symbol);
        if (indexFuture != null) {
            return indexFuture;
        } else {
            return null;
        }
    }

    @Override
    public void saveCurrentPriceFuture(int exId, String symbol, String contractType, QuanTradeFuture tradeFuture) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan.market.future.trade." + exId + "." + symbol);
        map.put(contractType, tradeFuture);
    }

    @Override
    public QuanTradeFuture getCurrentPriceFuture(int exId, String symbol, String contractType) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan.market.future.trade." + exId + "." + symbol);
        return map.get(contractType);
    }

    @Override
    public void saveCurrentPriceSpot(int exId, String symbol, QuanTrade trade) {
        RMap<String, QuanTrade> map = client.getMap("quan.market.spot.trade." + exId);
        map.put(symbol, trade);
    }

    @Override
    public QuanTrade getCurrentPriceSpot(int exchangeId, String symbol) {
        RMap<String, QuanTrade> map = client.getMap("quan.market.spot.trade." + exchangeId);
        return map.get(symbol);
    }

    @Override
    public void saveDepthSpot(int exchangeId, String symbol, List<QuanDepthDetail> list) {
        RMap<String, List<QuanDepthDetail>> map = client.getMap("quan.market.spot.depth." + exchangeId);
        map.put(symbol, list);
    }

    @Override
    public List<QuanDepthDetail> getDepthSpot(int exchangeId, String symbol) {
        RMap<String, List<QuanDepthDetail>> map = client.getMap("quan.market.spot.depth." + exchangeId);
        return map.get(symbol);
    }

    @Override
    public void saveKlineSpot(int exchangeId, String symbol, String period, List<QuanKline> quanKline) {
        RMap<String, List<QuanKline>> map = client.getMap("quan.market.spot.kline." + exchangeId + "." + period);
        map.put(symbol, quanKline);
    }

    @Override
    public List<QuanKline> getKlineSpot(int exchangeId, String symbol, String period) {
        RMap<String, List<QuanKline>> map = client.getMap("quan.market.spot.kline." + exchangeId + "." + period);
        return map.get(symbol);
    }

    @Override
    public List<QuanAccountAsset> getAccountSpot(long accountId, int exchangeId) {
        RMap<Long, List<QuanAccountAsset>> map = client.getMap("quan.account.spot.accountasset." + exchangeId);
        return map.get(accountId);
    }



}
