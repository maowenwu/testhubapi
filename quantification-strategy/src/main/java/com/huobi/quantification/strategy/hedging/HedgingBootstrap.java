package com.huobi.quantification.strategy.hedging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.huobi.quantification.entity.StrategyHedgingConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedging.service.CommonService;

@Component
public class HedgingBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StrategyProperties strategyProperties;

	@Autowired
	private StartHedging startHedging;

	@Autowired
	CommonService commonService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		/*if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
			logger.info("==>spring 容器启动");
			StrategyProperties.ConfigGroup group1 = strategyProperties.getGroup1();
			if (group1.getEnable()) {
				startWithConfig(group1);
			}
			StrategyProperties.ConfigGroup group2 = strategyProperties.getGroup2();
			if (group2.getEnable()) {
				startWithConfig(group2);
			}
			StrategyProperties.ConfigGroup group3 = strategyProperties.getGroup3();
			if (group3.getEnable()) {
				startWithConfig(group3);
			}
		}*/
	}

	private void startWithConfig(StrategyProperties.ConfigGroup group) {
		StartHedgingParam startHedgingParam = new StartHedgingParam();
		try {
			StrategyProperties.Config future = group.getFuture();
			StrategyProperties.Config spot = group.getSpot();
			StrategyHedgingConfig strategyHedgingConfig = commonService.getStrategyHedgingConfig(future.getExchangeId(),
					future.getContractCode());
			startHedgingParam.setBaseCoin(spot.getBaseCoin());
			startHedgingParam.setFeeRate(strategyHedgingConfig.getFormalityRate());
			startHedgingParam.setQuoteCoin(spot.getQuotCoin());
			startHedgingParam.setSlippage(strategyHedgingConfig.getSlippage());
			startHedgingParam.setSpotAccountID(spot.getAccountId());
			startHedgingParam.setSpotExchangeId(spot.getExchangeId());
			startHedgingParam.setFutureAccountID(future.getAccountId());
			startHedgingParam.setFutureExchangeId(future.getExchangeId());
		} catch (Exception e2) {
			logger.error("对冲启动出现异常", e2);
		}

		// 等待3秒，保证job已经完全运行
		sleep(30000000);
		while (true) {
			try {
				startHedging.startNormal(startHedgingParam);
				sleep(1000 * 60);
			} catch (Throwable e) {
				logger.error("对冲期间出现异常", e);
				sleep(5000);
			}
		}

	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {

		}
	}

}
