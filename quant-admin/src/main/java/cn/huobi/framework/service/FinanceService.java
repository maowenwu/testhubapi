package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.db.pagination.Page;

public interface FinanceService {

	List<StrategyFinanceHistory> selectByCondition(StrategyFinanceHistory config, Page<StrategyFinanceHistory> page);

	int insert(StrategyFinanceHistory riskConfig);

}
