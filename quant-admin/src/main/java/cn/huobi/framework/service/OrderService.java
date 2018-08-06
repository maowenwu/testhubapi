package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyOrderConfig;

import cn.huobi.framework.db.pagination.Page;

public interface OrderService {

	List<StrategyOrderConfig> selectByCondition(StrategyOrderConfig config, Page<StrategyOrderConfig> page);

	int updateOrder(StrategyOrderConfig riskConfig);

}
