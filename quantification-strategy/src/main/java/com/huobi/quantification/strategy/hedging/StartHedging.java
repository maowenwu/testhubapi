package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.strategy.hedging.service.AccountService;
import com.huobi.quantification.strategy.hedging.service.MarketService;
import com.huobi.quantification.strategy.hedging.service.OrderService;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;

@Component
public class StartHedging {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotOrderService spotOrderService;
	@Autowired
	AccountService accountService;


	@Autowired
	FutureContractService futureContractService;

	@Autowired
	MarketService  marketService;
	@Autowired
	OrderService orderService;

	@Autowired
	StrategyRiskConfigMapper strategyRiskConfigMapper;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

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
			BigDecimal positionUSDT = calUSDTPosition(startHedgingParam);
			logger.info("2.当前的两个账户总的净头寸USDT为  {}  ", positionUSDT);

			// 3. 获取买一卖一价格
			logger.info("3. 获取交易对  {} 买一卖一价格", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin());
			SpotDepthReqDto spotDepthReqDto = new SpotDepthReqDto();
			spotDepthReqDto.setBaseCoin(startHedgingParam.getBaseCoin());
			spotDepthReqDto.setQuoteCoin(startHedgingParam.getQuoteCoin());
			Map<String, BigDecimal> priceMap = marketService.getHuoBiSpotBuyOneSellOnePrice(spotDepthReqDto);
			logger.info("3. 交易对  {} 买一卖一价格为： {} ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(),
					JSON.toJSONString(priceMap));

			// 4. 下单
			logger.info("4.开始下对冲单");
			ServiceResult<SpotPlaceOrderRespDto> placeResult = orderService.placeHuobiSpotOrder(priceMap,
					startHedgingParam, positionUSDT);
			logger.info("4.下对冲单结果为： {}  ", JSON.toJSONString(placeResult));

		} catch (Exception e) {
			logger.error("对冲  {} 发生了异常： {}  ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), e);
		}
	}

	public void startSpecial(StartHedgingParam startHedgingParam) {

	}

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
		BigDecimal furureTotal = (futureUSDBalance = futureUSDBalance.subtract(futureUSDInitAmount)).divide(rateOfUSDT2USD, 8,
				BigDecimal.ROUND_HALF_DOWN);
		BigDecimal total=spotTotal.subtract(furureTotal);
		// 暂时只买一半
		return total.divide(new BigDecimal(2));
	}

}
