package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.QuanAccountFutureSecret;

import cn.huobi.framework.db.pagination.Page;

public interface FutureSecretService {

	List<QuanAccountFutureSecret> selectByCondition(QuanAccountFutureSecret secret, Page<QuanAccountFutureSecret> page);

	int update(QuanAccountFutureSecret secret);

	int insert(QuanAccountFutureSecret secret);

	int deleteById(Integer id);

}
