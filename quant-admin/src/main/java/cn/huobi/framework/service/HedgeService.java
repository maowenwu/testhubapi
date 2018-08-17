package cn.huobi.framework.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;

public interface HedgeService {

	int updateHedge(HedgeConfig riskConfig);

	PageInfo<HedgeConfig> selectByCondition(HedgeConfig config, PageInfo<HedgeConfig> page);
}
