package cn.huobi.framework.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
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
	private StrategyRiskConfigMapper strategyRiskConfigMapper;

	@Override
	public int updateRisk(StrategyRiskConfig riskConfig) {
		return strategyRiskConfigMapper.updateByPrimaryKeySelective(riskConfig);
	}

	@Override
	public PageInfo<StrategyRiskConfig> selectByCondition(StrategyRiskConfig config, PageInfo<StrategyRiskConfig> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StrategyRiskConfig> strategyRiskConfigs = strategyRiskConfigMapper.selectList(config);
        page = new PageInfo<>(strategyRiskConfigs);
        return page;
	}

}
