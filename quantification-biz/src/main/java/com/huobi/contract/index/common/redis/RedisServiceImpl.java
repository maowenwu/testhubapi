package com.huobi.contract.index.common.redis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.IpPool;
import org.redisson.api.RDeque;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean set(final String key, final String value) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    public String get(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public <T> boolean setList(String key, List<T> list) {
        String value = JSONObject.toJSONString(list);
        return set(key, value);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            List<T> list = JSONArray.parseArray(json, clz);
            return list;
        }
        return null;
    }

    @Override
    public long lpush(final String key, Object obj) {
        final String value = JSONObject.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public long rpush(final String key, Object obj) {
        final String value = JSONObject.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public String lpop(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res = connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }
    @Override
    public long llen(String key) {
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long length = redisConnection.lLen(serializer.serialize(key));
                return length;
            }
        });
        return result;
    }
    @Override
    public boolean set(String key, String value, long expireTime) {
        boolean result = set(key, value);
        if (result) {
            return expire(key, expireTime);
        }
        return result;
    }

    @Override
    public <T> boolean setList(String key, List<T> list, long expireTime) {
        boolean result = setList(key, list);
        if (result) {
            return expire(key, expireTime);
        }
        return result;
    }


    @Override
    public synchronized void updateIndexRealtimePrice(String exchange, String symbol, BigDecimal price) {
        boolean shutdown = redissonClient.isShutdown();
        System.out.println("--shutdown--->" + shutdown);
        RMap<String, BigDecimal> indexPriceMap = redissonClient.getMap(Constant.INDEX_REALTIME_PRICE_KEY + exchange);
        indexPriceMap.put(symbol, price);
    }

    @Override
    public Map<String, BigDecimal> getIndexRealtimePriceByExchange(String exchange) {
        return redissonClient.getMap(Constant.INDEX_REALTIME_PRICE_KEY + exchange);
    }

    @Override
    public void updateIndexRate(String symbol, BigDecimal price) {
        RMap<String, BigDecimal> indexRateMap = redissonClient.getMap(Constant.CURRENCY_RATE_KEY_PREFIX);
        indexRateMap.put(symbol, price);
    }

    @Override
    public BigDecimal getIndexRate(String symbol) {
        RMap<String, BigDecimal> indexRateMap = redissonClient.getMap(Constant.CURRENCY_RATE_KEY_PREFIX);
        return indexRateMap.get(symbol);
    }

    @Override
    public void updateLatestIndexPrice(String symbol, ContractPriceIndex index) {
        RMap<String, ContractPriceIndex> indexPriceMap = redissonClient.getMap(Constant.LAST_INDEX_PRICE_KEY_PREFIX);
        indexPriceMap.put(symbol, index);
    }

    @Override
    public ContractPriceIndex getLatestIndexPirce(String symbol) {
        RMap<String, ContractPriceIndex> indexPriceMap = redissonClient.getMap(Constant.LAST_INDEX_PRICE_KEY_PREFIX);
        return indexPriceMap.get(symbol);
    }

    @Override
    public void updateHttpHisIndexPrice(String exchange, String symbol, ContractPriceIndexHis index) {
        RMap<String, ContractPriceIndexHis> indexPriceMap = redissonClient.getMap(Constant.HISTORY_PRICE_HTTP_KEY_PREFIX + exchange);
        indexPriceMap.put(symbol, index);
    }

    @Override
    public ContractPriceIndexHis getHttpHisIndexPrice(String exchange, String symbol) {
        RMap<String, ContractPriceIndexHis> indexPriceMap = redissonClient.getMap(Constant.HISTORY_PRICE_HTTP_KEY_PREFIX + exchange);
        return indexPriceMap.get(symbol);
    }

    @Override
    public void updateWsHisIndexPrice(String exchange, String symbol, ContractPriceIndexHis index) {
        RMap<String, ContractPriceIndexHis> indexPriceMap = redissonClient.getMap(Constant.HISTORY_PRICE_WS_KEY_PREFIX + exchange);
        indexPriceMap.put(symbol, index);
    }

    @Override
    public ContractPriceIndexHis getWsHisIndexPrice(String exchange, String symbol) {
        RMap<String, ContractPriceIndexHis> indexPriceMap = redissonClient.getMap(Constant.HISTORY_PRICE_WS_KEY_PREFIX + exchange);
        return indexPriceMap.get(symbol);
    }
    @Override
    public synchronized IpPool getFristIpPoolAndRemove(String exchangeName){
        RDeque<IpPool> queue = redissonClient.getDeque(Constant.IPPOOL_QUEUE_KEY + exchangeName);
        IpPool ipPool = null;
        try {
            ipPool = queue.getFirst();
            queue.removeFirst();
        } catch (Exception e) {
            return null;
        }
        return ipPool;
    }
    @Override
    public void setFristIpPool(String exchangeName,IpPool ipPool){
        RDeque<IpPool> queue = redissonClient.getDeque(Constant.IPPOOL_QUEUE_KEY + exchangeName);
//        queue.removeIf(new Predicate<IpPool>() {
//            @Override
//            public boolean test(IpPool ipPool) {
//
//            }
//        })
        if(!queue.contains(ipPool)){
            queue.addFirst(ipPool);
        }
    }
    @Override
    public void setLastIpPool(String exchangeName,IpPool ipPool){
        RDeque<IpPool> queue = redissonClient.getDeque(Constant.IPPOOL_QUEUE_KEY + exchangeName);
        if(!queue.contains(ipPool)){
            queue.addLast(ipPool);
        }
    }
    @Override
    public int getDQueueLength(String exchangeName) {
        RDeque<IpPool> queue = redissonClient.getDeque(Constant.IPPOOL_QUEUE_KEY + exchangeName);
        return queue.size();
    }
}
