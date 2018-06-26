package com.huobi.contract.index.okcoin;

import java.io.IOException;

import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.okcoin.rest.future.IFutureRestApi;
import com.okcoin.rest.future.impl.FutureRestApiV1;

@Service
public class FutureClientFacadeImpl implements FutureClientFacade {
	private String api_key = "e26abcfe-8a11-45e7-9c01-fd3585913771"; // OKCoin申请的apiKey
	private String secret_key = "DBC9485DBFBFA73F2CC79EA04C4A612D"; // OKCoin 申请的secret_key
	private String url_prex = "https://www.okex.com"; // 注意：请求URL 国际站https://www.okcoin.com ; 国内站https:/
	private IFutureRestApi futureGetV1 = null;
	private IFutureRestApi futurePostV1 = null;

	public FutureClientFacadeImpl() {
		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 */
		futureGetV1 = new FutureRestApiV1(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入， 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		futurePostV1 = new FutureRestApiV1(url_prex, api_key, secret_key);
	}

	@Override
	public String future_ticker(String symbol, String contractType) throws HttpException, IOException {
		return futureGetV1.future_ticker(symbol, contractType);
	}

	@Override
	public String future_index(String symbol) throws HttpException, IOException {
		String indexStr = futureGetV1.future_index(symbol);
		return StringUtils.isNotEmpty(indexStr)
				? JSONObject.parseObject(indexStr).getBigDecimal("future_index").toString()
				: "";
	}

	@Override
	public String future_trades(String symbol, String contractType) throws HttpException, IOException {
		return futureGetV1.future_trades(symbol, contractType);
	}

	@Override
	public String future_depth(String symbol, String contractType) throws HttpException, IOException {
		return futureGetV1.future_depth(symbol, contractType);
	}

	@Override
	public String exchange_rate() throws HttpException, IOException {
		String rateJson = futureGetV1.exchange_rate();
		return StringUtils.isNotEmpty(rateJson) ? JSONObject.parseObject(rateJson).getBigDecimal("rate").toString()
				: "";
	}

	@Override
	public String future_cancel(String symbol, String contractType, String orderId) throws HttpException, IOException {
		return futurePostV1.future_cancel(symbol, contractType, orderId);
	}

	@Override
	public String future_trade(String symbol, String contractType, String price, String amount, String type,
			String matchPrice) throws HttpException, IOException {
		return futurePostV1.future_trade(symbol, contractType, price, amount, type, matchPrice);
	}

	@Override
	public String future_userinfo() throws HttpException, IOException {
		return futurePostV1.future_userinfo();
	}

	@Override
	public String future_userinfo_4fix() throws HttpException, IOException {
		return futurePostV1.future_userinfo_4fix();
	}

	@Override
	public String future_position(String symbol, String contractType) throws HttpException, IOException {
		return futurePostV1.future_position(symbol, contractType);
	}

	@Override
	public String future_position_4fix(String symbol, String contractType) throws HttpException, IOException {
		return futurePostV1.future_position_4fix(symbol, contractType);
	}

	@Override
	public String future_order_info(String symbol, String contractType, String orderId, String status,
			String currentPage, String pageLength) throws HttpException, IOException {
		return futurePostV1.future_order_info(symbol, contractType, orderId, status, currentPage, pageLength);
	}

}
