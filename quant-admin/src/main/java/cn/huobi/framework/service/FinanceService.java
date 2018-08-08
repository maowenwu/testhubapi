package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FinanceHistory;

public interface FinanceService {

	List<FinanceHistory> selectByCondition(FinanceHistory history, Page<FinanceHistory> page);

	int insert(StrategyFinanceHistory history);

}
