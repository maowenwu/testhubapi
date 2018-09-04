package com.huobi.common.api;

import java.io.IOException;

import org.apache.http.HttpException;

public class FutureClient {

	public static void main(String[] args) throws HttpException, IOException {
			
			/**
			 *  get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
			 */
		  	String api_key = "b3f6e555555599";  
	       	String secret_key = "70ff555555555e4";  
	 	    String url_prex = "http://api.hbdm.com";
			IFutureRestApi futureGetV1 = new FutureRestApiV1(url_prex);
			IFutureRestApi futurePostV1 = new FutureRestApiV1(url_prex, api_key,secret_key);

			//获取合约信息
			String contractInfo=futureGetV1.future_contract_info("BTC", "this_week", "");
			System.out.println("获取合约信息"+contractInfo);
			
			//获取合约指数信息
			String contractindex=futureGetV1.future_contract_index("BTC");
			System.out.println("获取合约指数信息"+contractindex);
			
			//获取合约最高限价和最低限价
			String pricelimit=futureGetV1.future_price_limit("BTC_CW", "this_week", "");
			System.out.println("获取合约最高限价和最低限价"+pricelimit);
			//获取当前可用合约总持仓量
			String openInterest=futureGetV1.future_open_interest("BTC_CW", "this_week", "");
			System.out.println("获取当前可用合约总持仓量"+openInterest);
			
			//获取行情深度数据
			String marketDepth=futureGetV1.future_market_depth("BTC_CW","step0");
			System.out.println("获取行情深度数据"+marketDepth);
			
			//获取K线数据
			String historyKline=futureGetV1.future_market_history_kline("BTC_CW","15min");
			System.out.println("获取K线数据"+historyKline);
			
			//获取聚合行情
			String merged=futureGetV1.future_market_detail_merged("BTC_CW");
			System.out.println("获取聚合行情"+merged);
			
			//获取市场最近成交记录
			String trade=futurePostV1.future_market_detail_trade("BTC_CW","10");
			System.out.println("获取市场最近成交记录"+trade);
			
			//批量获取最近的交易记录
			String historTrade=futurePostV1.future_market_history_trade("BTC_CW","10");
			System.out.println("批量获取最近的交易记录"+historTrade);
			//获取用户账户信息
			String accountInfo=futurePostV1.future_contract_account_info("BTC");
			System.out.println("获取用户账户信息"+accountInfo);
			
			//获取用户持仓信息
			String positionInfo=futurePostV1.future_contract_position_info("BTC");
			System.out.println("获取用户持仓信息"+positionInfo);
			
			//合约下单
			String contractOrder=futurePostV1.future_contract_order("BTC","this_week","BTC180907","","6759","12","buy","open","10","limit");
			
			System.out.println("合约下单返回"+contractOrder);
			
			//合约取消订单
			String contractcancel=futurePostV1.future_contract_cancel("123556","");
			System.out.println("合约取消订单"+contractcancel);
			
			//合约全部撤单
			String contractCancelall=futurePostV1.future_contract_cancelall("BTC");
			System.out.println("合约取消订单"+contractCancelall);
			
			//获取合约订单信息
			String contractOrderInfo=futurePostV1.future_contract_order_info("123556","");
			System.out.println("合约取消订单"+contractOrderInfo);
			
			//获取订单明细信息
			String detail=futurePostV1.future_contract_order_detail("BTC","123556","1","100");
			System.out.println("获取订单明细信息"+detail);
			
			//获取合约当前未成交委托
			String openorders=futurePostV1.future_contract_openorders("BTC","1","100");
			System.out.println("获取订单明细信息"+openorders);
			
			// 获取合约历史委托
			String orderDetail=futureGetV1.future_contract_hisorders("BTC","0","1","0","90","1","20");
			System.out.println("获取订单明细信息"+orderDetail);
	}

}
