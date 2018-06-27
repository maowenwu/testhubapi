package com.huobi.quantification.common.constant;

public class HttpConstant {

    /**
     * RestTemplate 读取超时时间
     */
    public static final Integer RESTTEMPLATE_READ_TIMEOUT = 5000;
    /**
     * RestTemplate 连接超时时间
     */
    public static final Integer RESTTEMPLATE_CONNECT_TIMEOUT = 5000;


    public static final String OK_ROOT = "https://www.okex.com/api/v1";
    public static final String OK_TICKER = OK_ROOT + "/future_ticker.do";
    public static final String OK_DEPTH = OK_ROOT + "/future_depth.do";
    public static final String OK_TRADES = OK_ROOT + "/future_trades.do";
    public static final String OK_INDEX = OK_ROOT + "/future_index.do";
}
