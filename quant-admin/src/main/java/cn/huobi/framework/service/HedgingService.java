package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyHedgingConfig;

import cn.huobi.framework.db.pagination.Page;

public interface HedgingService {

	List<StrategyHedgingConfig> selectDicByCondition(StrategyHedgingConfig config, Page<StrategyHedgingConfig> page);

	int updateHedging(StrategyHedgingConfig riskConfig);

}
