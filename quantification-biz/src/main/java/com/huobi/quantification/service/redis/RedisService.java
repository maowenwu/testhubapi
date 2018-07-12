package com.huobi.quantification.service.redis;

import com.huobi.quantification.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedisService {
    void saveOkUserInfo(Long accountId, List<QuanAccountFutureAsset> list);

    void saveOkPosition(Long accountId, String symbol, String type, List<QuanAccountFuturePosition> list);


    void saveOkTicker(String symbol, String contractType, QuanTickerFuture ticker);

    void saveOkDepth(String symbol, String contractType, List<QuanDepthFutureDetail> list);

    void saveOkKline(String symbol, String type, String contractType, List<QuanKlineFuture> redisKline);

    void saveHuobiTicker(String symbol, QuanTicker ticker);

    void saveHuobiDepth(String symbol, String type, QuanDepth depth);

    void saveHuobiAccount(QuanAccount quanAccount);

    void saveOkOrder(String symbol, String contractType, QuanOrderFuture orderFuture);

    Map<String, QuanOrderFuture> getOkOrder(Long accountId, String symbol, String contractType);

    void saveHuobiAccountAsset(QuanAccountAsset quanAccountAsset, Long accountId);

    void saveHuobiOrder(QuanOrder quanOrder);

    void saveIndexFuture(QuanIndexFuture quanIndexFuture);

    QuanIndexFuture getIndexFuture(int exchangeId, String symbol);
}
