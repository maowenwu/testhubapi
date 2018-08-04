package cn.huobi.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyHedgingConfig;

import cn.huobi.framework.dao.HadgingDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.HedgingService;

@Service("hedgingService")
@Transactional
public class HedgingServiceImpl implements HedgingService {
	
	@Autowired
	private HadgingDao hedgingDao;

	@Override
	public List<StrategyHedgingConfig> selectDicByCondition(StrategyHedgingConfig config,
			Page<StrategyHedgingConfig> page) {
		return hedgingDao.selectDicByCondition(config, page);
	}

	@Override
	public int updateHedging(StrategyHedgingConfig riskConfig) {
		return hedgingDao.update(riskConfig);
	}
}
