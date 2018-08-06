package cn.huobi.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.dao.HadgeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.HedgeService;

@Service("hedgeService")
@Transactional
public class HedgeServiceImpl implements HedgeService {
	
	@Autowired
	private HadgeDao hedgeDao;

	@Override
	public List<StrategyHedgeConfig> selectByCondition(StrategyHedgeConfig config,
			Page<StrategyHedgeConfig> page) {
		return hedgeDao.selectByCondition(config, page);
	}

	@Override
	public int updateHedge(StrategyHedgeConfig riskConfig) {
		return hedgeDao.update(riskConfig);
	}
}
