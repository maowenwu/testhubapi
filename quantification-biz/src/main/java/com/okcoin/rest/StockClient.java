package com.okcoin.rest;

import java.io.IOException;

import org.apache.http.HttpException;

import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;

/**
 * 现货 REST API 客户端请求
 * 
 * @author zhangchi
 *
 */
public class StockClient {

	public static void main(String[] args) throws HttpException, IOException {

		String api_key = "e26abcfe-8a11-45e7-9c01-fd3585913771"; // OKCoin申请的apiKey
		String secret_key = "DBC9485DBFBFA73F2CC79EA04C4A612D"; // OKCoin 申请的secret_key
		String url_prex = "https://www.okex.com"; // 注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn

		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 * 
		 */
		IStockRestApi stockGet = new StockRestApi(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入， 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);

		// 现货行情
		System.out.println(stockGet.ticker("btc_usdt"));

		// 现货市场深度
		System.out.println(stockGet.depth("btc_usdt"));

		// 现货OKCoin历史交易信息
		System.out.println(stockGet.trades("btc_usdt", "20"));

		// 现货用户信息
		//stockPost.userinfo();

		// 现货下单交易
		/*String tradeResult = stockPost.trade("btc_usd", "buy", "50", "0.02");
		System.out.println(tradeResult);
		JSONObject tradeJSV1 = JSONObject.parseObject(tradeResult);
		String tradeOrderV1 = tradeJSV1.getString("order_id");

		// 现货获取用户订单信息
		stockPost.order_info("btc_usd", tradeOrderV1);

		// 现货撤销订单
		stockPost.cancel_order("btc_usd", tradeOrderV1);

		// 现货批量下单
		stockPost.batch_trade("btc_usd", "buy", "[{price:50, amount:0.02},{price:50, amount:0.03}]");

		// 批量获取用户订单
		stockPost.orders_info("0", "btc_usd", "125420341, 125420342");

		// 获取用户历史订单信息，只返回最近七天的信息
		stockPost.order_history("btc_usd", "0", "1", "20");*/

	}
}
