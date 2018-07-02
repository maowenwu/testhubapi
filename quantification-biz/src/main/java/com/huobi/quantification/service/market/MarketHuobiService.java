package com.huobi.quantification.service.market;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface MarketHuobiService {
	/**
	 * GET /market/history/kline 获取K线数据
	 * @param symbol
	 * @return
	 */
	 Object getTicker(String symbol);
	 /**
	  * GET /market/depth 获取 Market Depth 数据
	  * @param symbol
	  * @param type
	  * @return
	  */
	 Object getDepth(String symbol,String type); 
	 /**
	  * GET /market/history/kline 获取K线数据
	  * @param symbol
	  * @param period
	  * @param size
	  * @return
	  */
	 Object getKline(String symbol,String period,String size);
}
