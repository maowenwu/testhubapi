package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
		hedgeConfig.setHedgeInterval(strategyHedgeConfig.getHedgeInterval());
		hedgeConfig.setSymbol(strategyHedgeConfig.getSymbol());
		hedgeConfig.setBuySlippage(strategyHedgeConfig.getBuySlippage());
		hedgeConfig.setSellSlippage(strategyHedgeConfig.getSellSlippage());
		hedgeConfig.setStopTime1(strategyHedgeConfig.getStopTime1());
		hedgeConfig.setStopTime2(strategyHedgeConfig.getStopTime2());
		hedgeConfig.setDeliveryInterval(strategyHedgeConfig.getDeliveryInterval());
		hedgeConfig.setDeliveryBuySlippage(strategyHedgeConfig.getDeliveryBuySlippage());
		hedgeConfig.setDeliverySellSlippage(strategyHedgeConfig.getDeliverySellSlippage());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		hedgeConfig.setCreateTime(formatter.format(strategyHedgeConfig.getCreateTime()));
		hedgeConfig.setUpdateTime(formatter.format(strategyHedgeConfig.getUpdateTime()));
		return hedgeConfig;
	}

	private StrategyHedgeConfig convertHedgeConfig(HedgeConfig config) {
		StrategyHedgeConfig hedgeConfig = new StrategyHedgeConfig();
		hedgeConfig.setContractType(config.getContractType());
		hedgeConfig.setId(config.getId());
		hedgeConfig.setHedgeInterval(config.getHedgeInterval());
		hedgeConfig.setSymbol(config.getSymbol());
		hedgeConfig.setBuySlippage(config.getBuySlippage());
		hedgeConfig.setSellSlippage(config.getSellSlippage());
		hedgeConfig.setStopTime1(config.getStopTime1());
		hedgeConfig.setStopTime2(config.getStopTime2());
		hedgeConfig.setDeliveryInterval(config.getDeliveryInterval());
		hedgeConfig.setDeliveryBuySlippage(config.getDeliveryBuySlippage());
		hedgeConfig.setDeliverySellSlippage(config.getDeliverySellSlippage());		
		hedgeConfig.setUpdateTime(new Date());
		return hedgeConfig;
	}

	@Override
	public int updateHedge(HedgeConfig config) {
		StrategyHedgeConfig hedgeConfig = convertHedgeConfig(config);
		return hedgeDao.update(hedgeConfig);
	}
}
