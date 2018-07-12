package cn.huobi.framework.service.sysUser.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huobi.framework.service.RedisService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("redisService")
@Transactional
public class RedisServiceImpl implements RedisService {
	@Resource
	protected RedisTemplate<String, Object> redisTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	@Override
	public boolean insertString(String key, String value) {
		try {
			 redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertString(String key, String value, Long expireTime) throws Exception {
		try {
			 redisTemplate.opsForValue().set(key, value);
			 redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertString(String key, String value, Date expireAtTime) throws Exception {
		try {
			 redisTemplate.opsForValue().set(key, value);
			 redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean insertList(String key, Object value) {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}

	}
	@Override
	public boolean insertList(String key, Object value, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertList(String key, Object value, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForList().leftPushAll(key, value);	
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey , Object value) {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey, Object value, Long expireTime) throws Exception {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean insertHash(String key, String sonKey, Object value, Date expireAtTime) throws Exception {
		try {
			redisTemplate.opsForHash().put(key, sonKey, value);	
			redisTemplate.expireAt(key, expireAtTime);
			return true;
		} catch (Exception e) {
			log.info("Redis新增错误:{}", e.getMessage());
			return false;
		}
	}
	@Override
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}
	@Override
	public Object select(String key){
		try {
			 DataType type = redisTemplate.type(key);
			 if(DataType.NONE == type){
//				 log.info("Redis key不存在");
				 return null;
			 }else if(DataType.STRING == type){
				 return redisTemplate.opsForValue().get(key);
			 }else if(DataType.LIST == type){
				 return redisTemplate.opsForList().range(key, 0, -1);
			 }else if(DataType.HASH == type){
				 return redisTemplate.opsForHash().entries(key);
			 }else
				 return null;
		} catch (Exception e) {
			log.info("Redis查询错误:{}", e.getMessage());
			return null;
		}
	}
	@Override
	public boolean delete(List<String> keys){
		try{
			redisTemplate.delete(keys);
			return true;
		}catch(Exception e){
			log.info("Redis 删除失败:{}", e.getMessage());
			return false;
		}
	}


	

}
