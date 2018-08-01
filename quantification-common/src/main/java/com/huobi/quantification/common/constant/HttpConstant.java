package com.huobi.quantification.common.constant;

public class HttpConstant {

    /********OK 期货********/
    public static final String OK_HOST = "https://www.okex.com";
    public static final String OK_CONTRACE_CODE = OK_HOST + "/api/v1/future_hold_amount.do";
    // OK market相关
    public static final String OK_TICKER = OK_HOST + "/api/v1/future_ticker.do";
    public static final String OK_DEPTH = OK_HOST + "/api/v1/future_depth.do";
    public static final String OK_KLINE = OK_HOST + "/api/v1/future_kline.do";
    public static final String OK_INDEX = OK_HOST + "/api/v1/future_index.do";
    public static final String OK_TRADES = OK_HOST + "/api/v1/future_trades.do";
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

    /********HUOBI 现货********/
    public static final String HUOBI_HOST = "https://api.huobipro.com";
    //火币market相关
    ///market/detail/merged 获取聚合行情(Ticker)
    public static final String HUOBI_TICKER = HUOBI_HOST + "/market/detail/merged";
    //GET /market/depth
    public static final String HUOBI_DEPTH = HUOBI_HOST + "/market/depth";
    ///market/history/kline
    public static final String HUOBI_KLINE = HUOBI_HOST + "/market/history/kline";
    //GET /market/trade 获取 Trade Detail 数据
    public static final String HUOBI_TRADE = HUOBI_HOST + "/market/trade";

    //火币账户相关 /v1/account/accounts/{account-id}/balance
    public static final String HUOBI_ACCOUNT = HUOBI_HOST + "/v1/account/accounts/{account-id}/balance";

    //火币订单相关    
    public static final String HUOBI_ORDER_PLACE = HUOBI_HOST + "/v1/order/orders/place";
    public static final String HUOBI_SUBMITCANCEL = HUOBI_HOST + "/v1/order/orders/{order-id}/submitcancel";
    public static final String HUOBI_BATCHCANCEL = HUOBI_HOST + "/v1/order/orders/batchcancel";
    public static final String HUOBI_ORDERDETAIL = HUOBI_HOST + "/v1/order/orders/{order-id}";
    public static final String HUOBI_OPENORDERS = HUOBI_HOST + "/v1/order/openOrders";
    public static final String HUOBI_MATCHRESULTS = HUOBI_HOST + "/v1/order/orders/{order-id}/matchresults";
    public static final String HUOBI_BATCHCANCELOPENORDERS = HUOBI_HOST + "/v1/order/orders/batchCancelOpenOrders";

    /********HUOBI 期货********/
    // market 相关
    public static final String HUOBI_FUTURE_TICKER = "http://127.0.0.1:8882/market/detail/merged";
    public static final String HUOBI_FUTURE_DEPTH = "http://127.0.0.1:8882/market/depth";
    public static final String HUOBI_FUTURE_KLINE = "http://127.0.0.1:8882/market/history/kline";
    public static final String HUOBI_FUTURE_TRADE = "http://127.0.0.1:8882/market/history/trade";

    // account相关
    public static final String HUOBI_FUTURE_ACCOUNTINFO = "http://www.huobiapps.com/contract-query/v1/contract_accountinfo";
    public static final String HUOBI_FUTURE_POSITION = "http://www.huobiapps.com/contract-query/v1/contract_position";
    public static final String HUOBI_FUTURE_ORDER = "http://www.huobiapps.com/contract-order/v1/contract_order";
    public static final String HUOBI_FUTURE_ORDER_CANCEL = "http://www.huobiapps.com/contract-order/v1/contract_cancel";
    public static final String HUOBI_FUTURE_ORDER_INFO = "http://www.huobiapps.com/contract-query/v1/contract_orderinfo";
    public static final String HUOBI_FUTURE_ORDER_CANCEL_ALL = "http://www.huobiapps.com/contract-order/v1/contract_cancelall";
    public static final String HUOBI_CONTRACE_CODE = "http://www.huobiapps.com/contract-query/v1/contract_hold_amount";

}
