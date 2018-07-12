package cn.huobi.framework.service;

import java.util.Date;
import java.util.List;

public interface RedisService {
	public boolean insertString(String key, String value) throws Exception;
	public boolean insertString(String key, String value, Long expireTime) throws Exception;
	public boolean insertString(String key, String value, Date expireAtTime) throws Exception;
	public boolean insertList(String key, Object value) throws Exception;
	public boolean insertList(String key, Object value, Long expireTime) throws Exception;
	public boolean insertList(String key, Object value, Date expireAtTime) throws Exception;
	public boolean insertHash(String key, String sonKey, Object value) throws Exception;
	public boolean insertHash(String key, String sonKey, Object value, Long expireTime) throws Exception;
	public boolean insertHash(String key, String sonKey, Object value, Date expireAtTime) throws Exception;
	public boolean exists(final String key) throws Exception;
	public Object select(String key) throws Exception;
	public boolean delete(List<String> keys) throws Exception;
	
}
