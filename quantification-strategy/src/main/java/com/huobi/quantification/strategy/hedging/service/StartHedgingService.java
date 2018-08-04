package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.strategy.hedging.entity.StartHedgingParam;
import com.huobi.quantification.strategy.hedging.service.AccountInfoService;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.hedging.service.MarketInfoService;
import com.huobi.quantification.strategy.hedging.service.OrderInfoService;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;
import com.huobi.quantification.strategy.hedging.utils.CommonUtil;

@Component
public class StartHedgingService {

	private Logger logger = LoggerFactory.getLogger(getClass());

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

	StartHedgingParam startHedgingParam;

	/**
	 * 启动普通的对冲
	 * 
	 * @param startHedgingParam
	 */
	public void startNormal(StartHedgingParam startHedgingParam) {
		// 0. 判断是否能够对冲
		/*
		 * StrategyRiskConfig
		 * strategyRiskConfig=strategyRiskConfigMapper.selectByContractCode(
		 * startHedgingParam.getContractCode());
		 * if(RiskHedgingTypeEnum.STOP_HADGING.getHadgingType()==strategyRiskConfig.
		 * getHedgingRiskManagement()) { return; //停止摆单 }
		 */
         if(!CommonUtil.isNormalHedgingDate()) {
        	 logger.info("当前时间不在正常对冲时间内");
        	 return;
         }
		
		// 1.撤掉币币账户所有未成交订单
		logger.info("1.开始撤掉火币现货账户所有未成交订单");
		Boolean result = orderInfoService.cancelOrder(startHedgingParam.getSpotAccountID(),
				startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), null, null);
		logger.info("1.撤掉火币现货账户所有未成交订单返回的结果为：{}", JSON.toJSONString(result));

		// 2.计算当前的两个账户总的净头寸USDT
		logger.info("2.开始计算当前的两个账户总的净头寸USDT");
		BigDecimal positionUSDT = commonService.calUSDTPosition(startHedgingParam);
		logger.info("2.当前的两个账户总的净头寸USDT为  {}  ", positionUSDT);

		// 3. 下单
		logger.info("3.开始下对冲单");
		Boolean placeResult = orderInfoService.placeHuobiSpotOrder(startHedgingParam, positionUSDT);
		logger.info("3.下对冲单结果为： {}  ", placeResult);

	}

	/**
	 * 交割处理
	 * 
	 * @param startHedgingParam
	 */
	public void startSpecial(StartHedgingParam startHedgingParam, Integer lastCount) {
		// 1.撤掉币币账户所有未成交订单
		logger.info("1.开始撤掉火币现货账户所有未成交订单");
		Boolean result = orderInfoService.cancelOrder(startHedgingParam.getSpotAccountID(),
				startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), null, null);
		logger.info("1.撤掉火币现货账户所有未成交订单返回的结果为：{}", JSON.toJSONString(result));
		// 2.计算当前的两个账户总的净头寸USDT
		logger.info("2.开始计算当前的两个账户总的净头寸USDT");
		BigDecimal positionUSDT = commonService.calUSDTPosition(startHedgingParam);
		logger.info("2.当前的两个账户总的净头寸USDT为  {}  ", positionUSDT);

		// 计算此时合约账户净空仓金额并折算为USDT
		BigDecimal futureUSDPosition = accountInfoService.getFutureUSDPosition(startHedgingParam.getFutureAccountID(),
				startHedgingParam.getFutureExchangeId(), startHedgingParam.getContractCode());
		// 2.5 获取USDT USD的汇率
		ServiceResult<BigDecimal> rateResult = futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD = rateResult.getData();
		BigDecimal futureUSDTPosition = futureUSDPosition.multiply(rateOfUSDT2USD);

		// 需要对冲的USDT
		BigDecimal needHedgingUSDT = futureUSDTPosition.subtract(positionUSDT);

		// 剩余需要对冲的金额/剩余需要对冲的次数
		needHedgingUSDT = needHedgingUSDT.divide(new BigDecimal(lastCount), 18, BigDecimal.ROUND_DOWN);

		// 4. 下单
		logger.info("交割期间开始下对冲单 ");
		Boolean placeResult = orderInfoService.placeHuobiSpotOrder(startHedgingParam, needHedgingUSDT);
		logger.info("交割期间下对冲单结果为： {}  ", placeResult);

	}

}
