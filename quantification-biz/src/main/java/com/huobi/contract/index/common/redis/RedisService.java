package com.huobi.contract.index.common.redis;

import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.IpPool;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedisService {

    boolean set(String key, String value);

    boolean set(String key, String value, long expireTime);

    String get(String key);

    boolean expire(String key, long expire);

    <T> boolean setList(String key, List<T> list);

    <T> boolean setList(String key, List<T> list, long expireTime);

    <T> List<T> getList(String key, Class<T> clz);

    long lpush(String key, Object obj);

    long rpush(String key, Object obj);

    String lpop(String key);
    /**
     * 获取list的长度
     * @param key
     * @return
     */
    long llen(String key);

    /*************redisson 实现*************/

    void updateIndexRealtimePrice(String exchange, String symbol, BigDecimal price);

    Map<String, BigDecimal> getIndexRealtimePriceByExchange(String exchange);

    /**
     * 更新指数汇率
     *
     * @param symbol
     * @param price
     */
    void updateIndexRate(String symbol, BigDecimal price);

    BigDecimal getIndexRate(String symbol);

    /**
     * 更新最新指数价格
     *
     * @param symbol
     * @param index
     */
    void updateLatestIndexPrice(String symbol, ContractPriceIndex index);


    ContractPriceIndex getLatestIndexPirce(String symbol);

    void updateHttpHisIndexPrice(String exchange, String symbol, ContractPriceIndexHis index);

    ContractPriceIndexHis getHttpHisIndexPrice(String exchange, String symbol);

    void updateWsHisIndexPrice(String exchange, String symbol, ContractPriceIndexHis index);

    ContractPriceIndexHis getWsHisIndexPrice(String exchange, String symbol);

    IpPool getFristIpPoolAndRemove(String exchangeName);

    void setFristIpPool(String exchangeName,IpPool ipPool);

    void setLastIpPool(String exchangeName,IpPool ipPool);

    int getDQueueLength(String exchangeName);
}
