package cn.huobi.framework.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.StrategyOrderConfig;

import cn.huobi.framework.db.pagination.Page;

public interface OrderService {

	int updateOrder(StrategyOrderConfig riskConfig);

	PageInfo<StrategyOrderConfig> selectByCondition(StrategyOrderConfig config, PageInfo<StrategyOrderConfig> page);
}
