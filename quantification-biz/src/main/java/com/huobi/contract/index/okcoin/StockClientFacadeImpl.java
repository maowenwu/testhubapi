package com.huobi.contract.index.okcoin;

import java.io.IOException;

import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import com.okcoin.rest.stock.IStockRestApi;
import com.okcoin.rest.stock.impl.StockRestApi;

@Service
public class StockClientFacadeImpl implements StockClientFacade {
	private String api_key = "e26abcfe-8a11-45e7-9c01-fd3585913771"; // OKCoin申请的apiKey
	private String secret_key = "DBC9485DBFBFA73F2CC79EA04C4A612D"; // OKCoin 申请的secret_key
	private String url_prex = "https://www.okex.com"; // 注意：请求URL 国际站https://www.okcoin.com ; 国内站https:/
	private IStockRestApi stockGet = null;
	private IStockRestApi stockPost = null;

	public StockClientFacadeImpl() {
		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 * 
		 */
		stockGet = new StockRestApi(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入， 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		stockPost = new StockRestApi(url_prex, api_key, secret_key);
	}

	@Override
	public String ticker(String symbol) throws HttpException, IOException {
		return stockGet.ticker(symbol);
	}

	@Override
	public String depth(String symbol) throws HttpException, IOException {
		return stockGet.depth(symbol);
	}

	@Override
	public String trades(String symbol, String since) throws HttpException, IOException {
		return stockGet.trades(symbol, since);
	}

	@Override
	public String userinfo() throws HttpException, IOException {
		return stockPost.userinfo();
	}

	@Override
	public String trade(String symbol, String type, String price, String amount) throws HttpException, IOException {
		return stockPost.trade(symbol, type, price, amount);
	}

	@Override
	public String batch_trade(String symbol, String type, String orders_data) throws HttpException, IOException {
		return stockPost.batch_trade(symbol, type, orders_data);
	}

	@Override
	public String cancel_order(String symbol, String order_id) throws HttpException, IOException {
		return stockPost.cancel_order(symbol, order_id);
	}

	@Override
	public String order_info(String symbol, String order_id) throws HttpException, IOException {
		return stockPost.order_info(symbol, order_id);
	}

	@Override
	public String orders_info(String type, String symbol, String order_id) throws HttpException, IOException {
		return stockPost.orders_info(type, symbol, order_id);
	}

	@Override
	public String order_history(String symbol, String status, String current_page, String page_length)
			throws HttpException, IOException {
		return stockPost.order_history(symbol, status, current_page, page_length);
	}

}
