package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.entity.QuanAccountFuture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huobi.framework.service.FutureAccountService;

@Service("FutureAccountService")
@Transactional
public class FutureAccountServiceImpl implements FutureAccountService{
	
	@Resource
	private QuanAccountFutureMapper quanAccountFutureMapper;

	@Override
	public int insert(QuanAccountFuture account) {
		return quanAccountFutureMapper.insertSelective(account);
	}

	@Override
	public int update(QuanAccountFuture account) {
		return quanAccountFutureMapper.updateByPrimaryKeySelective(account);
	}

	@Override
	public int deleteById(Long id) {
		return quanAccountFutureMapper.deleteByPrimaryKey(id);
	}

	@Override
	public PageInfo<QuanAccountFuture> selectByCondition(QuanAccountFuture account, PageInfo<QuanAccountFuture> page) {
		PageHelper.startPage(page.getPageNum(),page.getPageSize());
		List<QuanAccountFuture> quanAccountFutures = quanAccountFutureMapper.selectList(account);
		page = new PageInfo<>(quanAccountFutures);
		return page;
	}

}
