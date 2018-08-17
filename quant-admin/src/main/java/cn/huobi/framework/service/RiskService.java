package cn.huobi.framework.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.framework.db.pagination.Page;

public interface RiskService {

	int updateRisk(StrategyRiskConfig riskConfig);

	PageInfo<StrategyRiskConfig> selectByCondition(StrategyRiskConfig config, PageInfo<StrategyRiskConfig> page);
}
