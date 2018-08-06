package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.db.pagination.Page;

public interface HedgeService {

	List<StrategyHedgeConfig> selectByCondition(StrategyHedgeConfig config, Page<StrategyHedgeConfig> page);

	int updateHedge(StrategyHedgeConfig riskConfig);

}
