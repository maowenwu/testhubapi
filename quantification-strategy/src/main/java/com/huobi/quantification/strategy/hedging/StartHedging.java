package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.strategy.hedging.service.AccountInfoService;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.hedging.service.MarketInfoService;
import com.huobi.quantification.strategy.hedging.service.OrderInfoService;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;
import com.huobi.quantification.strategy.order.entity.FuturePosition;

@Component
public class StartHedging {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SpotOrderService spotOrderService;

	@Autowired
	FutureContractService futureContractService;

	@Autowired
	MarketInfoService marketInfoService;
	@Autowired
	OrderInfoService orderInfoService;

	@Autowired
	AccountInfoService accountInfoService;

	@Autowired
	StrategyRiskConfigMapper strategyRiskConfigMapper;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

	@Autowired
	CommonService commonService;

	/**
	 * 启动普通的对冲
	 * 
	 * @param startHedgingParam
	 */
	public void startNormal(StartHedgingParam startHedgingParam) {
		try {
			// 0. 判断是否能够对冲
			/*
			 * StrategyRiskConfig
			 * strategyRiskConfig=strategyRiskConfigMapper.selectByContractCode(
			 * startHedgingParam.getContractCode());
			 * if(RiskHedgingTypeEnum.STOP_HADGING.getHadgingType()==strategyRiskConfig.
			 * getHedgingRiskManagement()) { return; //停止摆单 }
			 */

			// 1.撤掉币币账户所有未成交订单
			logger.info("1.开始撤掉火币现货账户所有未成交订单");
			ServiceResult<Object> result = spotOrderService.cancelOrder(startHedgingParam.getSpotAccountID(),
					startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), null, null);
			logger.info("1.撤掉火币现货账户所有未成交订单返回的结果为：{}", JSON.toJSONString(result));

			// 2.计算当前的两个账户总的净头寸USDT
			logger.info("2.开始计算当前的两个账户总的净头寸USDT");
			BigDecimal positionUSDT = commonService.calUSDTPosition(startHedgingParam);
			logger.info("2.当前的两个账户总的净头寸USDT为  {}  ", positionUSDT);

			// 3. 下单
			logger.info("3.开始下对冲单");
			ServiceResult<SpotPlaceOrderRespDto> placeResult = orderInfoService.placeHuobiSpotOrder(startHedgingParam,
					positionUSDT);
			logger.info("3.下对冲单结果为： {}  ", JSON.toJSONString(placeResult));

		} catch (Exception e) {
			logger.error("对冲  {} 发生了异常： {}  ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), e);
		}
	}

	/**
	 * 交割处理
	 * 
	 * @param startHedgingParam
	 */
	public void startSpecial(StartHedgingParam startHedgingParam) {
		// 2.计算当前的两个账户总的净头寸USDT
		logger.info("2.开始计算当前的两个账户总的净头寸USDT");
		BigDecimal positionUSDT = commonService.calUSDTPosition(startHedgingParam);
		logger.info("2.当前的两个账户总的净头寸USDT为  {}  ", positionUSDT);

		// 计算此时合约账户净空仓金额并折算为USDT
		FuturePosition futurePosition = accountInfoService.getFuturePosition(startHedgingParam.getFutureAccountID(),
				startHedgingParam.getFutureExchangeId(), startHedgingParam.getContractCode());
		BigDecimal shortAmountUSD = futurePosition.getShortPosi().getShortAmount();
		BigDecimal longAmountUSD = futurePosition.getLongPosi().getLongAmount();
		BigDecimal totalAmountUSD = shortAmountUSD.subtract(longAmountUSD);
		// 2.5 获取USDT USD的汇率
		ServiceResult<BigDecimal> rateResult = futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD = rateResult.getData();
		logger.info("2.3 USDT/USD的利率为：{}", rateResult.getData());
		BigDecimal totalAmountUSDT = totalAmountUSD.divide(rateOfUSDT2USD, 8, BigDecimal.ROUND_HALF_DOWN);

		// 需要对冲的USDT
		BigDecimal needHedgingUSDT = totalAmountUSDT.subtract(positionUSDT);

		// 需要对冲的次数为
		Integer count = (new BigDecimal(3300)).divide(new BigDecimal(60), 1, BigDecimal.ROUND_HALF_DOWN).intValue();
		// 每次需要对冲的金额
		needHedgingUSDT = needHedgingUSDT.divide(new BigDecimal(count));

		// 4. 下单
		for (int i = 1; i <= count; i++) {
			logger.info("交割期间开始下对冲单,当前交割次数为 {} ", i);
			ServiceResult<SpotPlaceOrderRespDto> placeResult = orderInfoService.placeHuobiSpotOrder(startHedgingParam,
					positionUSDT);
			logger.info("交割期间第 {} 次下对冲单结果为： {}  ", i, JSON.toJSONString(placeResult));
		}

	}

}
