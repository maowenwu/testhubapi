package com.huobi.quantification.service.redis.impl;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanDepthDetail;
import com.huobi.quantification.entity.QuanDepthFutureDetail;
import com.huobi.quantification.entity.QuanIndexFuture;
import com.huobi.quantification.entity.QuanKlineFuture;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.entity.QuanOrderFuture;
import com.huobi.quantification.entity.QuanTicker;
import com.huobi.quantification.entity.QuanTickerFuture;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.service.redis.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedissonClient client;

	@Override
	public void saveFutureUserInfo(int exchangeId, Long accountId, QuanAccountFutureAsset futureAsset) {
		RMap<Long, QuanAccountFutureAsset> map = client.getMap("quan.account.future.userinfo." + exchangeId);
		map.put(accountId, futureAsset);
	}

	@Override
	public QuanAccountFutureAsset getFutureUserInfo(int exchangeId, Long accountId) {
		RMap<Long, QuanAccountFutureAsset> map = client.getMap("quan.account.future.userinfo." + exchangeId);
		return map.get(accountId);
	}

	@Override
	public void saveFuturePosition(int exchangeId, Long accountId, QuanAccountFuturePosition position) {
		RMap<Long, QuanAccountFuturePosition> map = client.getMap("quan.account.future.position." + exchangeId);
		map.put(accountId, position);
	}

	@Override
	public QuanAccountFuturePosition getFuturePosition(int exchangeId, Long accountId) {
		RMap<Long, QuanAccountFuturePosition> map = client.getMap("quan.account.future.position." + exchangeId);
		return map.get(accountId);
	}

	@Override
	public void saveOkTicker(String symbol, String contractType, QuanTickerFuture ticker) {
		RMap<String, Object> map = client.getMap("quan.market.future.ok.ticker." + symbol);
		map.put(contractType, ticker);
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
	public void saveHuobiTicker(int exchangeId, String symbol, QuanTicker ticker) {
		RMap<String, Object> map = client.getMap("quan.market.spot.huobi.ticker." + exchangeId);
		map.put(symbol, ticker);
	}

	@Override
	public void saveHuobiAccount(QuanAccount quanAccount) {
		RMap<String, Object> map = client.getMap("quan.account.huobi");
		map.put(String.valueOf(quanAccount.getAccountSourceId()), quanAccount);
	}

	@Override
	public void saveOkOrder(String symbol, String contractType, QuanOrderFuture orderFuture) {
		RMap<String, Object> map = client
				.getMap("quan.order.ok." + orderFuture.getOrderAccountId() + "." + symbol + "." + contractType);
		map.put(genOkOrderKey(orderFuture), orderFuture);
	}

	private String genOkOrderKey(QuanOrderFuture orderFuture) {
		return "" + orderFuture.getOrderSourceId();
	}

	@Override
	public RMap<String, QuanOrderFuture> getOkOrder(Long accountId, String symbol, String contractType) {
		RMap<String, QuanOrderFuture> map = client
				.getMap("quan.order.ok." + accountId + "." + symbol + "." + contractType);
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

	@Override
	public void saveHuobiDepth(int exchangeId, String symbol, QuanDepth depth, List<QuanDepthDetail> list) {
		RMap<String, List<QuanDepthDetail>> map = client.getMap("quan.market.spot.depth." + exchangeId);
		map.put(symbol, list);
	}

	@Override
	public List<QuanDepthDetail> getHuobiDepth(int exchangeId, String symbol) {
		RMap<String, List<QuanDepthDetail>> map = client.getMap("quan.market.spot.depth." + exchangeId);
		return map.get(symbol);
	}
}
