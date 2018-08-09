package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.QuanAccountFutureSecret;

import cn.huobi.framework.dao.FutureSecretDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.FutureSecretService;

@Service("futureSecretService")
@Transactional
public class FutureSecretServiceImpl implements FutureSecretService {
	
	@Resource
	private FutureSecretDao futureSecretDao;
	
	@Override
	public List<QuanAccountFutureSecret> selectByCondition(QuanAccountFutureSecret secret,
			Page<QuanAccountFutureSecret> page) {
		return futureSecretDao.selectByCondition(secret, page);
	}

	@Override
	public int update(QuanAccountFutureSecret secret) {
		return futureSecretDao.update(secret);
	}

	@Override
	public int insert(QuanAccountFutureSecret secret) {
		return futureSecretDao.insert(secret);
	}

	@Override
	public int deleteById(Integer id) {
		return futureSecretDao.delete(id);
	}

}
