package com.huobi.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;

import com.huobi.contract.index.dto.OrdersDto;
import com.okcoin.rest.HttpUtilManager;
import com.okcoin.rest.MD5Util;
import com.okcoin.rest.StringUtil;

public class HuobiAplUtil {

	private static String secret_key;
	
	private static String api_key;
	
	private static String url_prex="https://api.huobipro.com";
	
	/**
	 * 获取聚合行情L
	 */
	private static final String TICKER_URL = "/market/detail/merged?";
	public static String ticker(String symbol) throws HttpException, IOException {
		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		String param = "";
		if(!StringUtil.isEmpty(symbol )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;
		}
		String result = httpUtil.requestHttpGet(url_prex, TICKER_URL, param);
	    return result;
	}
	
	/**
	 * 现货行情URL
	 */
	private static final String KLINE_URL = "/market/history/kline?";
	public static String kline(String symbol,String period,String size) throws HttpException, IOException{
		HttpUtilManager httpUtil = HttpUtilManager.getInstance();
		String param = "";
		if(!StringUtil.isEmpty(symbol )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;		
		}
		if(!StringUtil.isEmpty(period )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "period=" + period;		
		}
		if(!StringUtil.isEmpty(size )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "size=" + size;		
		}
		
		String result = httpUtil.requestHttpGet(url_prex, KLINE_URL, param);
	    return result;
	}
	private static final String TICKERS_URL = "/market/tickers?";
	 public String tickers() throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			
			String result = httpUtil.requestHttpGet(url_prex, TICKERS_URL, param);
		    return result;
	 }
	 private  static final String DEPTH_URL = "/market/depth?";
	 public String depth(String symbol,String type) throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			if(!StringUtil.isEmpty(symbol )) {
				if (!param.equals("")) {
					param += "&";
				}
				param += "symbol=" + symbol;		
			}
			if(!StringUtil.isEmpty(type )) {
				if (!param.equals("")) {
					param += "&";
				}
				param += "type=" + type;		
			}
			String result = httpUtil.requestHttpGet(url_prex, DEPTH_URL, param);
		    return result;
	 }
	 private static final String  ACCOUNTURL= "/v1/account/accounts/{account-id}/balance";
	 public static String accounts(String accountId)throws HttpException, IOException{
		 HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String param = "";
			ACCOUNTURL.replace("{account-id}", accountId);
			String result = httpUtil.requestHttpGet(url_prex, ACCOUNTURL, param);
		    return result;
	 }
	 private static final String  ORDERSPLACE_URL= "/v1/order/orders/place";
	 public static String orders_place(OrdersDto ordersDto)
				throws HttpException, IOException {
			// 构造参数签名
			Map<String, String> params = new HashMap<String, String>();
			if (!StringUtil.isEmpty(ordersDto.getAccountId() )) {
				params.put("account-id", ordersDto.getAccountId());
			}
			if (!StringUtil.isEmpty(ordersDto.getAmount() )) {
				params.put("amount", ordersDto.getAmount());
			}
			if (!StringUtil.isEmpty(ordersDto.getPrice() )) {
				params.put("price", ordersDto.getPrice());
			}
			if (!StringUtil.isEmpty(api_key )) {
				params.put("api_key", api_key);
			}
			if (!StringUtil.isEmpty(ordersDto.getSource() )) {
				params.put("source", ordersDto.getSource());
			}
			if (!StringUtil.isEmpty(ordersDto.getSymbol() )) {
				params.put("symbol", ordersDto.getSymbol());
			}
			if (!StringUtil.isEmpty(ordersDto.getType() )) {
				params.put("type", ordersDto.getType());
			}
			String sign = MD5Util.buildMysignV1(params, secret_key);
			params.put("sign", sign);
			// 发送post请求

			HttpUtilManager httpUtil = HttpUtilManager.getInstance();
			String result = httpUtil.requestHttpPost(url_prex,ORDERSPLACE_URL,
					params);
			// System.out.println(result);
			return result;
		}	
	 	private static final String  SUBMITCANCEL_ORDER= "/v1/order/orders/place";
	 	public static String submitcancel_order(String order_id) throws HttpException, IOException {
	 				// 构造参数签名
					Map<String, String> params = new HashMap<String, String>();
					if (!StringUtil.isEmpty(order_id )) {
						params.put("order-id", order_id);
					}
					String sign = MD5Util.buildMysignV1(params, secret_key);
					params.put("sign", sign);
					// 发送post请求
					HttpUtilManager httpUtil = HttpUtilManager.getInstance();
					String result = httpUtil.requestHttpPost(url_prex,SUBMITCANCEL_ORDER,
							params);
					return result;
	 	}
}
