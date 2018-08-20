package com.huobi.quantification.service.redis;

import com.huobi.quantification.entity.*;

import java.util.List;
import java.util.Map;

public interface RedisService {

    /***************期货*********************/
    void saveAccountFuture(int exchangeId, Long accountId, Map<String, QuanAccountFutureAsset> assetMap);
    Map<String, QuanAccountFutureAsset> getAccountFuture(int exchangeId, Long accountId);

    void savePositionFuture(int exchangeId, Long accountId, List<QuanAccountFuturePosition> futurePositions);
    List<QuanAccountFuturePosition> getPositionFuture(int exchangeId, Long accountId);

    void saveDepthFuture(int exchangeId, String symbol, String contractType, String depthType, List<QuanDepthFutureDetail> list);
    List<QuanDepthFutureDetail> getDepthFuture(int exchangeId, String symbol, String contractType,String depthType);


    void saveIndexFuture(QuanIndexFuture quanIndexFuture);
    QuanIndexFuture getIndexFuture(int exchangeId, String symbol);

    void saveCurrentPriceFuture(int exId, String symbol, String contractType, QuanTradeFuture tradeFuture);
    QuanTradeFuture getCurrentPriceFuture(int exId, String symbol, String contractType);

    /***************现货*********************/
    void saveDepthSpot(int exchangeId, String symbol, List<QuanDepthDetail> list);
    List<QuanDepthDetail> getDepthSpot(int exchangeId, String symbol);

    void saveAccountSpot(List<QuanAccountAsset> quanAssetList, int exchangId, long accountId);
    List<QuanAccountAsset> getAccountSpot(long accountId, int exchangeId);

    void saveCurrentPriceSpot(int exId, String symbol, QuanTrade trade);
    QuanTrade getCurrentPriceSpot(int exchangeId, String symbol);



}
