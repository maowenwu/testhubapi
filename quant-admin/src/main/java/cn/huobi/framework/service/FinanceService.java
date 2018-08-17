package cn.huobi.framework.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FinanceHistory;

public interface FinanceService {

	int insert(StrategyFinanceHistory history);

	PageInfo<FinanceHistory> selectByCondition(FinanceHistory history, PageInfo<FinanceHistory> page);
}
