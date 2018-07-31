package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;

@Service
public class CommonService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AccountService accountService;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

	@Autowired
	FutureContractService futureContractService;

	/**
	 * 计算当前的两个账户总的净头寸USDT
	 * 
	 * @param startHedgingParam
	 * @return
	 */
	public BigDecimal calUSDTPosition(StartHedgingParam startHedgingParam) {

		// 2.1 获取火币现货账户期末USDT余额
		BigDecimal spotUSDTBalance = accountService.getHuobiSpotCurrentBalance(startHedgingParam.getSpotAccountID(),
				startHedgingParam.getSpotExchangeId(), startHedgingParam.getQuoteCoin()).getAvailable();
		// 2.2获取火币现货账户期初USDT余额
		BigDecimal spotUSDTInitAmount = quanAccountFuturePositionService.getInitAmount(
				startHedgingParam.getSpotAccountID(), startHedgingParam.getSpotExchangeId(), "spot", "usdt");

		// 2.3 获取火币期货账户期末USD余额
		BigDecimal futureUSDBalance = accountService.getHuobiFutureBalance(startHedgingParam.getFutureAccountID(),
				startHedgingParam.getFutureExchangeId(), startHedgingParam.getContractCode()).getMarginAvailable();
		// 2.4 获取火币期货账户期初USD余额
		BigDecimal futureUSDInitAmount = quanAccountFuturePositionService.getInitAmount(
				startHedgingParam.getSpotAccountID(), startHedgingParam.getSpotExchangeId(), "future", "usd");

		// 2.5 获取USDT USD的汇率
		ServiceResult<BigDecimal> rateResult = futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD = rateResult.getData();
		logger.info("2.3 USDT/USD的利率为：{}", rateResult.getData());

		// 2.6 计算净头寸
		BigDecimal spotTotal = spotUSDTBalance = spotUSDTBalance.subtract(spotUSDTInitAmount);
		BigDecimal furureTotal = (futureUSDBalance = futureUSDBalance.subtract(futureUSDInitAmount))
				.divide(rateOfUSDT2USD, 8, BigDecimal.ROUND_HALF_DOWN);
		BigDecimal total = spotTotal.subtract(furureTotal);
		// 暂时只买一半
		return total.divide(new BigDecimal(2));
	}

}
