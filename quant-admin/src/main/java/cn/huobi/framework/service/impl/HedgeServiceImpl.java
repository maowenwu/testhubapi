package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.dao.HadgeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;
import cn.huobi.framework.service.HedgeService;

@Service("hedgeService")
@Transactional
public class HedgeServiceImpl implements HedgeService {
	
	@Autowired
	private HadgeDao hedgeDao;

	@Override
	public List<HedgeConfig> selectByCondition(HedgeConfig config,
			Page<HedgeConfig> page) {
		StrategyHedgeConfig hedgeConfig = convertHedgeConfig(config);
		List<StrategyHedgeConfig> sConfigs = hedgeDao.selectByCondition(hedgeConfig, page);
		List<HedgeConfig> hedgeConfigs = new ArrayList<>();
		for (StrategyHedgeConfig strategyHedgeConfig : sConfigs) {
			HedgeConfig hConfig = convertStrategyHedgeConfig(strategyHedgeConfig);
			hedgeConfigs.add(hConfig);
		}
		return hedgeConfigs;
	}

	private HedgeConfig convertStrategyHedgeConfig(StrategyHedgeConfig strategyHedgeConfig) {
		HedgeConfig hedgeConfig = new HedgeConfig();
		hedgeConfig.setContractType(strategyHedgeConfig.getContractType());
		hedgeConfig.setId(strategyHedgeConfig.getId());
		hedgeConfig.setPlaceOrderInterval(strategyHedgeConfig.getPlaceOrderInterval());
		hedgeConfig.setSlippage(strategyHedgeConfig.getSlippage());
		hedgeConfig.setSpotFee(strategyHedgeConfig.getSpotFee());
		hedgeConfig.setSymbol(strategyHedgeConfig.getSymbol());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		hedgeConfig.setCreateTime(formatter.format(strategyHedgeConfig.getCreateTime()));
		hedgeConfig.setUpdateTime(formatter.format(strategyHedgeConfig.getUpdateTime()));
		return hedgeConfig;
	}

	private StrategyHedgeConfig convertHedgeConfig(HedgeConfig config) {
		StrategyHedgeConfig hedgeConfig = new StrategyHedgeConfig();
		return hedgeConfig;
	}

	@Override
	public int updateHedge(StrategyHedgeConfig riskConfig) {
		return hedgeDao.update(riskConfig);
	}
}
