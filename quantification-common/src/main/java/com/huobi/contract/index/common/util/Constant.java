package com.huobi.contract.index.common.util;

public class Constant {
    /**
     * 中位数short_name
     */
    public static final String MEDIAN_SHORT_NAME = "median";

    /**
     * S级报警,报警方式: 邮件
     */
    public static final String INDEX_MONITOR_EMAIL = "1";

    public static final Integer HISTORY_TIME_INTERVAL_OME_MINUTE = 1*60;

    public static final Integer HISTORY_TIME_INTERVAL_FIFTEEN_MINUTE = 15*60;

    public static final Integer HISTORY_TIME_INTERVAL_FIVE_MINUTE = 5*60;

    /**
     * A级报警,报警方式: 短信
     */
    public static final String INDEX_MONITOR_MESSAGE = "2";

    /**
     * 最新抓取价格历史价格 redis key
     */
    public static final String HISTORY_PRICE_HTTP_KEY_PREFIX = "contract.his.http.price.";

    public static final String HISTORY_PRICE_WS_KEY_PREFIX = "contract.his.ws.price.";
    /**
     * 最新指数价格 redis key
     */
    public static final String LAST_INDEX_PRICE_KEY_PREFIX = "contract.index.price";
    /**
     * 最新汇率 redis key
     */
    public static final String CURRENCY_RATE_KEY_PREFIX = "contract.rate";
    /**
     * IP队列 redis key
     */
    public static final String IPPOOL_QUEUE_KEY = "ip.pool.";
    /**
     * 最新指数历史价格redis 过期时间
     */
    public static final Long CONTRACT_HIS_REDIS_EXPIRE_TIME = 10 * 60 * 1000L;
    /**
     * 最新指数价格 redis 过期时间
     */
    public static final Long CONTRACT_INDEX_REDIS_EXPIRE_TIME = 10 * 60 * 1000L;
    /**
     * 最新汇率 redis过期价格
     */
    public static final Long EXCHANGE_RATE_REDIS_EXPIRE_TIME = 10 * 60 * 1000L;

    public static final Integer REQUEST_MANY_REQUEST_CODE = 429;

    public static final String USD_SHORTNAME = "USD";

    public static final Long DEFAULT_EXCHANGEID = 1L;
    public static final Integer RATE_GRAB_RETRY_COUNT = 3;
    /**
     * RestTemplate 读取超时时间
     */
    public static final Integer RESTTEMPLATE_READ_TIMEOUT = 5000;
    /**
     * RestTemplate 连接超时时间
     */
    public static final Integer RESTTEMPLATE_CONNECT_TIMEOUT = 5000;
    /**
     * 价格计算保留的位数
     */
    public static final Integer PRICE_DECIMALS_NUM = 4;

    public static final String INDEX_REALTIME_PRICE_KEY = "contract.ws.index.realtimeprice.";
}
