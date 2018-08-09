package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.QuanAccountSecret;

import cn.huobi.framework.dao.SpotSecretDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.SpotSecretService;

@Service("spotSecretService")
@Transactional
public class SpotSecretServiceImpl implements SpotSecretService{
	
	@Resource
	private SpotSecretDao spotSecretDao;

	@Override
	public List<QuanAccountSecret> selectByCondition(QuanAccountSecret secret, Page<QuanAccountSecret> page) {
		return spotSecretDao.selectByCondition(secret, page);
	}

	@Override
	public int update(QuanAccountSecret secret) {
		return spotSecretDao.update(secret);
	}

	@Override
	public int insert(QuanAccountSecret secret) {
		return spotSecretDao.insert(secret);
	}

	@Override
	public int deleteById(Integer id) {
		return spotSecretDao.delete(id);
	}

}
