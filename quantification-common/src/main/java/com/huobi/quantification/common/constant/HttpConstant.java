package com.huobi.quantification.common.constant;

public class HttpConstant {


    public static final String OK_HOST = "https://www.okex.com";
    // OK market相关
    public static final String OK_TICKER = OK_HOST + "/api/v1/future_ticker.do";
    public static final String OK_DEPTH = OK_HOST + "/api/v1/future_depth.do";
    public static final String OK_KLINE = OK_HOST + "/api/v1/future_kline.do";
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

    public static final String HUOBI_HOST = "https://api.huobipro.com";
    //火币market相关
    ///market/detail/merged 获取聚合行情(Ticker)
    public static final String HUOBI_TICKER = HUOBI_HOST + "/market/detail/merged";
    //GET /market/depth
    public static final String HUOBI_DEPTH = HUOBI_HOST + "/market/depth";
    ///market/history/kline
    public static final String HUOBI_KLINE = HUOBI_HOST + "/market/history/kline";
    
    //火币账户相关 /v1/account/accounts/{account-id}/balance
    public static final String HUOBI_ACCOUNT = HUOBI_HOST + "/v1/account/accounts/{account-id}/balance";
    
    //火币订单相关    
    public static final String HUOBI_ORDER_PLACE = HUOBI_HOST + "/v1/order/orders/place";
    public static final String HUOBI_SUBMITCANCEL = HUOBI_HOST + "/v1/order/orders/{order-id}/submitcancel";
    public static final String HUOBI_BATCHCANCEL = HUOBI_HOST + "/v1/order/orders/batchcancel";
    public static final String HUOBI_ORDERDETAIL = HUOBI_HOST + "/v1/order/orders/{order-id}";
    public static final String HUOBI_OPENORDERS = HUOBI_HOST + "/v1/order/openOrders";
    public static final String HUOBI_MATCHRESULTS = HUOBI_HOST + "/v1/order/orders/{order-id}/matchresults";
}
