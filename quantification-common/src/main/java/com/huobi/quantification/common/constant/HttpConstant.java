package com.huobi.quantification.common.constant;

public class HttpConstant {

    /********HUOBI 现货********/
    public static final String HUOBI_HOST = "https://api.huobipro.com";
    //火币market相关
    public static final String HUOBI_TICKER = HUOBI_HOST + "/market/detail/merged";
    public static final String HUOBI_DEPTH = HUOBI_HOST + "/market/depth";
    public static final String HUOBI_KLINE = HUOBI_HOST + "/market/history/kline";
    public static final String HUOBI_TRADE = HUOBI_HOST + "/market/trade";
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
    public static final String HUOBI_FUTURE_TICKER = "http://172.18.6.16:8882/market/detail/merged";
    public static final String HUOBI_FUTURE_DEPTH = "http://172.18.6.16:8882/market/depth";
    public static final String HUOBI_FUTURE_KLINE = "http://172.18.6.16:8882/market/history/kline";
    public static final String HUOBI_FUTURE_TRADE = "http://172.18.6.16:8882/market/history/trade";

    // account相关
    public static final String HUOBI_FUTURE_ACCOUNT_INFO = "http://www.huobiapps.com/api/v1/contract_account_info";
    public static final String HUOBI_FUTURE_POSITION_INFO = "http://www.huobiapps.com/api/v1/contract_position_info";
    public static final String HUOBI_FUTURE_ORDER = "http://www.huobiapps.com/api/v1/contract_order";
    public static final String HUOBI_FUTURE_ORDER_CANCEL = "http://www.huobiapps.com/api/v1/contract_cancel";
    public static final String HUOBI_FUTURE_ORDER_INFO = "http://www.huobiapps.com/api/v1/contract_order_info";
    public static final String HUOBI_FUTURE_ORDER_CANCEL_ALL = "http://www.huobiapps.com/api/v1/contract_cancelall";
    public static final String HUOBI_CONTRACE_CODE = "http://www.huobiapps.com/api/v1/contract_open_interest";
    public static final String HUOBI_CONTRACE_OPENORDERS="http://www.huobiapps.com/api/v1/contract_openorders";

}
