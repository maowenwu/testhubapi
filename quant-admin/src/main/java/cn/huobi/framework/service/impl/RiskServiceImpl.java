package cn.huobi.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.framework.dao.RiskDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.RiskService;

@Service("riskService")
@Transactional
public class RiskServiceImpl implements RiskService {
	
	@Autowired
	private RiskDao riskDao;

	@Override
	public List<StrategyRiskConfig> selectByCondition(StrategyRiskConfig config, Page<StrategyRiskConfig> page) {
		return riskDao.selectDicByCondition(config,page);
	}

	@Override
	public int updateRisk(StrategyRiskConfig riskConfig) {
		return riskDao.update(riskConfig);
	}

}
