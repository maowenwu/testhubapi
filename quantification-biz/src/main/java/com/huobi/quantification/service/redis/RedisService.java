package com.huobi.quantification.service.redis;

import java.util.List;
import java.util.Map;

import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKline;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.entity.QuanTickerFuture;
import com.huobi.quantification.entity.QuanTrade;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.huobi.response.TradeResponse;

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

	void setHuobiCurrentPrice(int exId, String symbol, QuanTrade trade);

	QuanTrade getHuobiCurrentPrice(int exchangeId, String symbol);

	void saveKline(int exchangeId, String symbol, String period, List<QuanKline> quanKline);

	List<QuanKline> getKlineSpot(int exchangeId, String symbol, String period);

	List<QuanAccountAsset> getSpotUserInfo(long accountId, int exchangeId);

	void saveFirstFutureAccountInfo(Long accountId, String contractCode, String body);

	String getFirstFutureAccountInfo(Long accountId, String contractCode);
	
	void saveFirstSpotAccounts(List<QuanAccountAsset> list, long accountId, String contractCode);
	
	List<QuanAccountAsset> getFirstSpotAccounts(long accountId, String contractCode);

	void saveFirstFuturePosition(Long accountId, String contractCode, String body);
	
	String getFirstFuturePosition(Long accountId, String contractCode);

}
