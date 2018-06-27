package com.huobi.quantification.common.constant;

public class HttpConstant {


    public static final String OK_HOST = "https://www.okex.com";
    // OK market相关
    public static final String OK_TICKER = OK_HOST + "/api/v1/future_ticker.do";
    public static final String OK_DEPTH = OK_HOST + "/api/v1/future_depth.do";
    // OK订单相关
    public static final String OK_ORDER_INFO = OK_HOST + "/api/v1/future_order_info.do";
    public static final String OK_ORDERS_INFO = OK_HOST + "/api/v1/future_orders_info.do";
    public static final String OK_TRADES_HISTORY = OK_HOST + "/api/v1/future_trades_history.do";
    public static final String OK_TRADE = OK_HOST + "/api/v1/future_trade.do";
    public static final String OK_CANCEL = OK_HOST + "/api/v1/future_cancel.do";
    public static final String OK_BATCH_TRADE = OK_HOST + "/api/v1/future_batch_trade.do";
    // OK account相关
    public static final String OK_USER_INFO = OK_HOST + "/api/v1/future_userinfo.do";
    public static final String OK_POSITION = OK_HOST + "/api/v1/future_position.do";


}
