package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huobi.framework.dao.SpotAccountDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotAccount;
import cn.huobi.framework.service.SpotAccountService;

@Service("spotAccountService")
@Transactional
public class SpotAccountServiceImpl implements SpotAccountService{
	
	@Resource
	private SpotAccountDao spotAccountDao;

	@Override
	public List<SpotAccount> selectByCondition(SpotAccount account, Page<SpotAccount> page) {
		return spotAccountDao.selectByCondition(account, page);
	}

	@Override
	public int insert(SpotAccount account) {
		return spotAccountDao.insert(account);
	}

	@Override
	public int update(SpotAccount account) {
		return spotAccountDao.update(account);
	}

	@Override
	public int deleteById(Integer id) {
		return spotAccountDao.delete(id);
	}

}
