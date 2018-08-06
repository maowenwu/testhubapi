package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyHedgingConfig;

import cn.huobi.framework.db.pagination.Page;

public interface HedgeService {

	List<StrategyHedgingConfig> selectByCondition(StrategyHedgingConfig config, Page<StrategyHedgingConfig> page);

	int updateHedge(StrategyHedgingConfig riskConfig);

}
