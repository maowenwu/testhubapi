package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.dao.FinanceDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.FinanceService;

@Service("financeService")
@Transactional
public class FinanceServiceImpl implements FinanceService {
	
	@Resource FinanceDao financeDao;

	@Override
	public List<StrategyFinanceHistory> selectByCondition(StrategyFinanceHistory config,
			Page<StrategyFinanceHistory> page) {
		return financeDao.selectByCondition(config, page);
	}

	@Override
	public int insert(StrategyFinanceHistory config) {
		return financeDao.insert(config);
	}

}
