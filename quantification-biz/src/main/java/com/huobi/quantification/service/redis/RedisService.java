package com.huobi.quantification.service.redis;

import com.huobi.quantification.entity.*;
import com.huobi.quantification.huobi.response.TradeResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedisService {
    void saveFutureUserInfo(int exchangeId, Long accountId, QuanAccountFutureAsset futureAsset);

    QuanAccountFutureAsset getFutureUserInfo(int exchangeId, Long accountId);

    void saveFuturePosition(int exchangeId, Long accountId, QuanAccountFuturePosition position);

    QuanAccountFuturePosition getFuturePosition(int exchangeId, Long accountId);


    void saveOkTicker(String symbol, String contractType, QuanTickerFuture ticker);

    void saveDepthFuture(int exchangeId, String symbol, String contractType, List<QuanDepthFutureDetail> list);

    List<QuanDepthFutureDetail> getDepthFuture(int exchangeId, String symbol, String contractType);

    void saveKlineFuture(int exchangeId, String symbol, String type, String contractType, List<QuanKlineFuture> redisKline);

    List<QuanKlineFuture> getKlineFuture(int exchangeId, String symbol, String type, String contractType);

    void saveHuobiTicker(int exchangeId, String symbol, QuanTicker ticker);

    void saveHuobiDepth(int exchangeId, String symbol, List<QuanDepthDetail> list);

    List<QuanDepthDetail> getHuobiDepth(int exchangeId, String symbol);

    void saveOkOrder(String symbol, String contractType, QuanOrderFuture orderFuture);

    Map<String, QuanOrderFuture> getOkOrder(Long accountId, String symbol, String contractType);

    void saveHuobiAccountAsset(List<QuanAccountAsset> quanAssetList, long accountId, int exchangId);

    void saveHuobiOrder(QuanOrder quanOrder);

    void saveIndexFuture(QuanIndexFuture quanIndexFuture);

    QuanIndexFuture getIndexFuture(int exchangeId, String symbol);

    void saveCurrentPrice(int exId, String symbol, String contractType, QuanTradeFuture tradeFuture);

    QuanTradeFuture getCurrentPrice(int exId, String symbol, String contractType);

	void setHuobiCurrentPrice(int exId, String symbol, TradeResponse trade);

	TradeResponse getHuobiCurrentPrice(int exchangeId, String symbol);

	void saveKline(int exchangeId, String symbol, String period, List<QuanKline> quanKline);

	List<QuanKline> getKlineSpot(int exchangeId, String symbol, String period);

	List<QuanAccountAsset> getSpotUserInfo(long accountId, int exchangeId);
}
