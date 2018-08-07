package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.dao.FinanceDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FinanceHistory;
import cn.huobi.framework.service.FinanceService;

@Service("financeService")
@Transactional
public class FinanceServiceImpl implements FinanceService {
	
	@Resource FinanceDao financeDao;

	@Override
	public List<FinanceHistory> selectByCondition(FinanceHistory config,
			Page<FinanceHistory> page) {
		StrategyFinanceHistory history = convertFinanceHistory(config);
		List<StrategyFinanceHistory> histories = financeDao.selectByCondition(history, page);
		List<FinanceHistory> financeHistories = new ArrayList<>();
		for (StrategyFinanceHistory strategyFinanceHistory : histories) {
			FinanceHistory financeHistory = convertStrategyFinanceHistory(strategyFinanceHistory);
			financeHistories.add(financeHistory);
		}
		return financeHistories;
	}

	private FinanceHistory convertStrategyFinanceHistory(StrategyFinanceHistory strategyFinanceHistory) {
		FinanceHistory financeHistory = new FinanceHistory();
		financeHistory.setAccountId(strategyFinanceHistory.getAccountId());
		financeHistory.setCoinType(strategyFinanceHistory.getCoinType());
		financeHistory.setExchangeId(strategyFinanceHistory.getExchangeId());
		if (strategyFinanceHistory.getMoneyType() == 1) {
			financeHistory.setMoneyType("充值");
		}else if (strategyFinanceHistory.getMoneyType() == 2) {
			financeHistory.setMoneyType("提现");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		financeHistory.setCreateTime(formatter.format(strategyFinanceHistory.getCreateTime()));
		financeHistory.setUpdateTime(formatter.format(strategyFinanceHistory.getUpdateTime()));
		financeHistory.setTransferAmount(strategyFinanceHistory.getTransferAmount());
		return financeHistory;
	}

	private StrategyFinanceHistory convertFinanceHistory(FinanceHistory config) {
		StrategyFinanceHistory history = new StrategyFinanceHistory();
		history.setAccountId(config.getAccountId());
		history.setExchangeId(config.getExchangeId());
		history.setCoinType(config.getCoinType());
		return history;
	}

	@Override
	public int insert(StrategyFinanceHistory config) {
		return financeDao.insert(config);
	}

}
