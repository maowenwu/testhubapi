package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.framework.db.pagination.Page;

public interface RiskService {

	List<StrategyRiskConfig> selectDicByCondition(StrategyRiskConfig config, Page<StrategyRiskConfig> page);

	int updateSpotJob(StrategyRiskConfig riskConfig);

}
