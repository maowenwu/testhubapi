package cn.huobi.framework.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.entity.QuanAccountHistory;

import cn.huobi.framework.service.QuanAccountHistoryService;

@Service("quanAccountHistoryService")
@Transactional
public class QuanAccountHistoryImpl implements QuanAccountHistoryService {
	@Resource
	QuanAccountHistoryMapper quanAccountHistoryMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(QuanAccountHistory record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public QuanAccountHistory selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuanAccountHistory> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKey(QuanAccountHistory record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getInitAmount(Long accountId, int exchangeId, String accountsType, String coin) {
		BigDecimal result=quanAccountHistoryMapper.getInitAmount(accountId, exchangeId, accountsType, coin);
		return result;
	}

}