package cn.huobi.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyHedgingConfig;

import cn.huobi.framework.dao.HadgeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.HedgeService;

@Service("hedgeService")
@Transactional
public class HedgeServiceImpl implements HedgeService {
	
	@Autowired
	private HadgeDao hedgeDao;

	@Override
	public List<StrategyHedgingConfig> selectByCondition(StrategyHedgingConfig config,
			Page<StrategyHedgingConfig> page) {
		return hedgeDao.selectByCondition(config, page);
	}

	@Override
	public int updateHedge(StrategyHedgingConfig riskConfig) {
		return hedgeDao.update(riskConfig);
	}
}
