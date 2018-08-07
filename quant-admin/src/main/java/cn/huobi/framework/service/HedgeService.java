package cn.huobi.framework.service;

import java.util.List;

import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;

public interface HedgeService {

	List<HedgeConfig> selectByCondition(HedgeConfig config, Page<HedgeConfig> page);

	int updateHedge(StrategyHedgeConfig riskConfig);

}
