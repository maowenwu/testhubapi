package com.huobi.quantification.service.market;

import com.huobi.quantification.entity.QuanDepth;
import com.huobi.quantification.entity.QuanTicker;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface MarketHuobiService {
	/**
	 * GET /market/history/kline 获取K线数据
	 * 
	 * @param symbol
	 * @return
	 */
	Object getTicker(String symbol);

	/**
	 * GET /market/depth 获取 Market Depth 数据
	 * 
	 * @param symbol
	 * @param type
	 * @return
	 */
	Object getDepth(String symbol, String type);

	/**
	 * GET /market/history/kline 获取K线数据
	 * 
	 * @param symbol
	 * @param period
	 * @param size
	 * @return
	 */
	Object getKline(String symbol, String period, String size);

	/**
	 * 获取所提供的所有ticker数据，保存到数据库
	 */
	void storeHuobiTicker();

	/**
	 * 获取所提供的所有Depth数据，保存到数据库
	 */
	void storeHuobiDepth();
}
