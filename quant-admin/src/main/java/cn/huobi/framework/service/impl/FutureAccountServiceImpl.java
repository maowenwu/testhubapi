package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huobi.framework.dao.FutureAccountDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureAccount;
import cn.huobi.framework.service.FutureAccountService;

@Service("FutureAccountService")
@Transactional
public class FutureAccountServiceImpl implements FutureAccountService{
	
	@Resource
	private FutureAccountDao FutureAccountDao;

	@Override
	public List<FutureAccount> selectByCondition(FutureAccount account, Page<FutureAccount> page) {
		return FutureAccountDao.selectByCondition(account, page);
	}

	@Override
	public int insert(FutureAccount account) {
		return FutureAccountDao.insert(account);
	}

	@Override
	public int update(FutureAccount account) {
		return FutureAccountDao.update(account);
	}

	@Override
	public int deleteById(Integer id) {
		return FutureAccountDao.delete(id);
	}

}
