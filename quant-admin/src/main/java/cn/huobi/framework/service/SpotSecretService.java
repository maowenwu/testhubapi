package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.QuanAccountSecret;

import cn.huobi.framework.db.pagination.Page;

public interface SpotSecretService {

	List<QuanAccountSecret> selectByCondition(QuanAccountSecret proxy, Page<QuanAccountSecret> page);

	int update(QuanAccountSecret proxy);

	int insert(QuanAccountSecret proxy);

	int deleteById(Integer id);

}
