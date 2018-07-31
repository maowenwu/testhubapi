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
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.hedging.service.QuanAccountFuturePositionService;

@Component
public class StartHedging {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotOrderService spotOrderService;
	@Autowired
	AccountUtil accountUtil;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	FutureContractService futureContractService;

	@Autowired
	MarketUtil marketUtil;
	@Autowired
	OrderUtil orderUtil;

	@Autowired
	StrategyRiskConfigMapper strategyRiskConfigMapper;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

	/**
	 * 启动普通的对冲
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
			Map<String, BigDecimal> priceMap = marketUtil.getHuoBiSpotBuyOneSellOnePrice(spotDepthReqDto);
			logger.info("3. 交易对  {} 买一卖一价格为： {} ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(),
					JSON.toJSONString(priceMap));

			// 4. 下单
			logger.info("4.开始下对冲单");
			ServiceResult<SpotPlaceOrderRespDto> placeResult = orderUtil.placeHuobiSpotOrder(priceMap,
					startHedgingParam, positionUSDT);
			logger.info("4.下对冲单结果为： {}  ", JSON.toJSONString(placeResult));

		} catch (Exception e) {
			logger.error("对冲  {} 发生了异常： {}  ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(), e);
		}
	}

	public void startSpecial(StartHedgingParam startHedgingParam) {

	}

	/**
	 * 计算USDT寸头
	 * 
	 * @param startHedgingParam
	 * @return
	 */
	public BigDecimal calUSDTPosition(StartHedgingParam startHedgingParam) {
		// 2.计算当前的两个账户总的净头寸USDT

		// 2.1 获取火币现货账户期末USDT余额
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(startHedgingParam.getSpotAccountID());
		spotBalanceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotBalanceReqDto.setMaxDelay(1000 * 60 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		com.huobi.quantification.dto.SpotBalanceRespDto.DataBean spotDataBean = accountUtil
				.getHuobiSpotCurrentBalance(spotBalanceReqDto, startHedgingParam.getQuoteCoin());
		BigDecimal spotUSDTBalance = spotDataBean.getAvailable();

		// 获取火币现货账户期初USDT余额
		BigDecimal spotUSDTInitAmount = quanAccountFuturePositionService.getInitAmount(
				startHedgingParam.getSpotAccountID(), startHedgingParam.getSpotExchangeId(), "spot", "usdt");
		spotUSDTBalance = spotUSDTBalance.subtract(spotUSDTInitAmount);

		// 2.2 获取火币期货账户期末USD余额 ===========暂时没有接口
		BigDecimal futureUSDBalance = spotDataBean.getAvailable();

		// 获取火币期货账户期初USD余额
		BigDecimal futureUSDInitAmount = quanAccountFuturePositionService.getInitAmount(
				startHedgingParam.getSpotAccountID(), startHedgingParam.getSpotExchangeId(), "future", "usd");
		futureUSDBalance = futureUSDBalance.subtract(futureUSDInitAmount);

		// 2.3 获取USDT USD的汇率
		ServiceResult<BigDecimal> rateResult = futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD = rateResult.getData();
		logger.info("2.3 USDT/USD的利率为：{}", rateResult.getData());

		// 2.4 计算净头寸
		BigDecimal total1 = (spotUSDTBalance.subtract(new BigDecimal(0)));
		BigDecimal total2 = (futureUSDBalance.subtract(new BigDecimal(0))).divide(rateOfUSDT2USD, 8,
				BigDecimal.ROUND_HALF_DOWN);
		// 暂时只买一半
		return total1.divide(new BigDecimal(2));
	}

}
