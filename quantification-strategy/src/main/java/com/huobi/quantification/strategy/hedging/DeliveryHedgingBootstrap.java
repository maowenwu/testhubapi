package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.entity.StrategyHedgingConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedging.entity.StartHedgingParam;
import com.huobi.quantification.strategy.hedging.service.AccountInfoService;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;
import com.huobi.quantification.strategy.hedging.service.StartHedgingService;
import com.huobi.quantification.strategy.hedging.utils.CommonUtil;

@Component
public class DeliveryHedgingBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StrategyProperties strategyProperties;

	@Autowired
	private StartHedgingService startHedgingService;

	@Autowired
	CommonService commonService;

	@Autowired
	AccountInfoService accountInfoService;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

	@Autowired
	JobManageService jobManageService;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
			logger.info("==交割期间对冲程序初始化......");
			/*StrategyProperties.ConfigGroup group1 = strategyProperties.getGroup1();
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
			}*/
		}
	}

	@SuppressWarnings("rawtypes")
	private void startWithConfig(StrategyProperties.ConfigGroup group) {
		StartHedgingParam startHedgingParam = new StartHedgingParam();
		logger.info("交割期间对冲注册job开始");
		jobManageService.addHuobiSpotAccountJob(group.getSpot().getAccountId(), "0/1 * * * * ?", true);
		jobManageService.addHuobiSpotDepthJob(group.getSpot().getBaseCoin() + group.getSpot().getQuotCoin(), "step1",
				"0/1 * * * * ?", true);
		jobManageService.addHuobiFuturePositionJob(group.getFuture().getAccountId(), "0/1 * * * * ?", true);
		logger.info("交割期间注册job完成");
		try {
			initHedgingParam(group, startHedgingParam);
		} catch (Exception e) {
			logger.error("交割期间对冲启动初始化参数异常", e);
			return;
		}
		logger.info("对冲启动初始化参数为 {} ", JSON.toJSON(startHedgingParam));
		// 等待3秒，保证job已经完全运行
		sleep(1000 * 3);
		logger.info("交割期间初始化对冲参数完成");
		Long period = 60L;// t秒对冲一次
		final String jobID = "my_job_1";
		final AtomicInteger count = new AtomicInteger(0);
		final Map<String, Future> futures = new HashMap<>();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		// 需要对冲的次数
		Integer totalCount = (new BigDecimal(3300)).divide(new BigDecimal(period), 1, BigDecimal.ROUND_HALF_DOWN)
				.intValue();
		Future future = scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Long end;
				Long begin = System.currentTimeMillis();
				Integer currentCount = count.get();
				if (count.get() > totalCount && CommonUtil.isNormalHedgingDate()) {
					Future future = futures.get(jobID);
					if (future != null)
						future.cancel(true);
					countDownLatch.countDown();
				}
				try {
					startHedgingService.startSpecial(startHedgingParam, totalCount - currentCount);
					end = System.currentTimeMillis();
					logger.info("第{}次交割期间正常对冲期间耗时:{}s", currentCount, (end - begin) / 1000);
				} catch (Throwable e) {
					end = System.currentTimeMillis();
					logger.info("第{}次交割期间对冲异常,耗时:{}s", count.get(), (end - begin) / 1000);
					logger.error("交割期间对冲期间出现异常,", e);
					sleep(1000 * 1);
				}

			}
		}, 0, period, TimeUnit.SECONDS);

		futures.put(jobID, future);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
		}
		scheduler.shutdown();
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
				startHedgingParam.getSpotExchangeId(), startHedgingParam.getQuoteCoin());
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
