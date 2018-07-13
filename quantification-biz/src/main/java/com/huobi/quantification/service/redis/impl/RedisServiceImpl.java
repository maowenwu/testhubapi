package com.huobi.quantification.service.redis.impl;

import com.huobi.quantification.entity.*;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.service.redis.RedisService;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void saveDepthFuture(int exchangeId, String symbol, String contractType, List<QuanDepthFutureDetail> list) {
        RMap<String, List<QuanDepthFutureDetail>> map = client.getMap("quan.market.future.depth." + exchangeId + "." + symbol);
        map.put(contractType, list);
    }

    @Override
    public List<QuanDepthFutureDetail> getDepthFuture(int exchangeId, String symbol, String contractType) {
        RMap<String, List<QuanDepthFutureDetail>> map = client.getMap("quan.market.future.depth." + exchangeId + "." + symbol);
        return map.get(contractType);
    }

    @Override
    public void saveKlineFuture(int exchangeId, String symbol, String type, String contractType, List<QuanKlineFuture> redisKline) {
        RMap<String, List<QuanKlineFuture>> map = client.getMap("quan.market.future.kline." + exchangeId + "." + type + "." + symbol);
        map.put(contractType, redisKline);
    }

    @Override
    public List<QuanKlineFuture> getKlineFuture(int exchangeId, String symbol, String type, String contractType) {
        RMap<String, List<QuanKlineFuture>> map = client.getMap("quan.market.future.kline." + exchangeId + "." + type + "." + symbol);
        return map.get(contractType);
    }

    @Override
    public void saveHuobiTicker(String symbol, QuanTicker ticker) {
        RMap<String, Object> map = client.getMap("quan.market.huobi.ticker." + symbol);
        map.put(symbol, ticker);
    }

    @Override
    public void saveHuobiDepth(String symbol, String type, QuanDepth depth) {
        RMap<String, Object> map = client.getMap("quan.market.huobi.depth." + symbol + "." + type);
        map.put(symbol, depth);
    }

    @Override
    public void saveHuobiAccount(QuanAccount quanAccount) {
        RMap<String, Object> map = client.getMap("quan.account.huobi");
        map.put(String.valueOf(quanAccount.getAccountSourceId()), quanAccount);
    }

    @Override
    public void saveOkOrder(String symbol, String contractType, QuanOrderFuture orderFuture) {
        RMap<String, Object> map = client.getMap("quan.order.ok." + orderFuture.getOrderAccountId() + "." + symbol + "." + contractType);
        map.put(genOkOrderKey(orderFuture), orderFuture);
    }

    private String genOkOrderKey(QuanOrderFuture orderFuture) {
        return "" + orderFuture.getOrderSourceId();
    }

    @Override
    public RMap<String, QuanOrderFuture> getOkOrder(Long accountId, String symbol, String contractType) {
        RMap<String, QuanOrderFuture> map = client.getMap("quan.order.ok." + accountId + "." + symbol + "." + contractType);
        return map;
    }

    @Override
    public void saveHuobiAccountAsset(QuanAccountAsset quanAccountAsset, Long accountId) {
        RMap<String, Object> map = client.getMap("quan.accout.huobi.accountasset." + accountId);
        map.put(quanAccountAsset.getCoin(), quanAccountAsset);
    }

    @Override
    public void saveHuobiOrder(QuanOrder quanOrder) {
        RMap<String, Object> map = client.getMap("quan.order.huobi" + quanOrder.getOrderSourceId());
        map.put(quanOrder.getOrderAccountId() + "", quanOrder);
    }

    @Override
    public void saveIndexFuture(QuanIndexFuture quanIndexFuture) {
        RMap<String, QuanIndexFuture> map = client.getMap("quan.market.future.index." + quanIndexFuture.getExchangeId());
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
    public void saveCurrentPrice(int exId, String symbol, String contractType, QuanTradeFuture tradeFuture) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan.market.future.trade." + exId + "." + symbol);
        map.put(contractType, tradeFuture);
    }

    @Override
    public QuanTradeFuture getCurrentPrice(int exId, String symbol, String contractType) {
        RMap<String, QuanTradeFuture> map = client.getMap("quan.market.future.trade." + exId + "." + symbol);
        return map.get(contractType);
    }

	@Override
	public void setHuobiCurrentPrice(int exId, String symbol, TradeResponse trade) {
		RMap<String, TradeResponse> map = client.getMap("quan.market.spot.trade." + exId);
		map.put(symbol, trade);
	}

	@Override
	public TradeResponse getHuobiCurrentPrice(int exchangeId, String symbol) {
		RMap<String, TradeResponse> map = client.getMap("quan.market.spot.trade." + exchangeId);
		return map.get(symbol);
	}
}
