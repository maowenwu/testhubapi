package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.entity.StrategyHedgingConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedging.service.AccountInfoService;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;

@Component
public class HedgingBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StrategyProperties strategyProperties;

	@Autowired
	private StartHedging startHedging;

	@Autowired
	CommonService commonService;

	@Autowired
	AccountInfoService accountInfoService;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

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
			initHedgingParam(group, startHedgingParam);
		} catch (Exception e) {
			logger.error("对冲启动初始化参数异常", e);
			return;
		}

		logger.info("对冲启动初始化参数为",JSON.toJSON(startHedgingParam));
		// 等待3秒，保证job已经完全运行
		sleep(1000 * 3);

		logger.info("初始化对冲参数完成");
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					startHedging.startNormal(startHedgingParam);
					sleep(1000 * 60);
				} catch (Throwable e) {
					logger.error("对冲期间出现异常", e);
					sleep(1000 * 10);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();

	}

	private StartHedgingParam initHedgingParam(StrategyProperties.ConfigGroup group,
			StartHedgingParam startHedgingParam) {
		StrategyProperties.Config future = group.getFuture();
		StrategyProperties.Config spot = group.getSpot();
		StrategyHedgingConfig strategyHedgingConfig = commonService.getStrategyHedgingConfig(future.getExchangeId(),
				future.getContractCode(), future.getBaseCoin());
		startHedgingParam.setBaseCoin(spot.getBaseCoin());
		startHedgingParam.setFeeRate(strategyHedgingConfig.getFormalityRate());
		startHedgingParam.setQuoteCoin(spot.getQuotCoin());
		startHedgingParam.setSlippage(strategyHedgingConfig.getSlippage());
		startHedgingParam.setSpotAccountID(spot.getAccountId());
		startHedgingParam.setSpotExchangeId(spot.getExchangeId());
		startHedgingParam.setFutureAccountID(future.getAccountId());
		startHedgingParam.setFutureExchangeId(future.getExchangeId());
		startHedgingParam.setContractCode(future.getContractCode());

		// 策略启动时调用 可以理解为期初的值
		// 2.1 获取火币现货账户期初USDT余额
		BigDecimal spotInitUSDT = accountInfoService.getHuobiSpotCurrentBalance(startHedgingParam.getSpotAccountID(),
				startHedgingParam.getSpotExchangeId(), startHedgingParam.getQuoteCoin()).getAvailable();
		// 2.2 获取火币期货账户期初净空仓金额USD
		BigDecimal futureInitUSD = new BigDecimal(0);
		futureInitUSD = accountInfoService.getFutureUSDPosition(startHedgingParam.getFutureAccountID(),
				startHedgingParam.getFutureExchangeId(), startHedgingParam.getContractCode());
		startHedgingParam.setSpotInitUSDT(spotInitUSDT);
		startHedgingParam.setFutureInitUSD(futureInitUSD);

		return startHedgingParam;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {

		}
	}

}
