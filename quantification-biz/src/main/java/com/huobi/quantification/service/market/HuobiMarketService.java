package com.huobi.quantification.service.market;

import com.huobi.quantification.response.spot.HuobiSpotDepthResponse;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiMarketService {

	/**
	 * GET /market/depth 获取 Market Depth 数据
	 * 
	 * @param symbol
	 * @param type
	 * @return
	 */
	HuobiSpotDepthResponse queryDepthByAPI(String symbol, String type);

	/**
	 * 获取所提供的所有Depth数据，保存到数据库
	 * 
	 * @param symbol
	 * @param depthType
	 */
	void updateHuobiDepth(String symbol,String depthType);
	
	void updateCurrentPrice(String symbol);

}
