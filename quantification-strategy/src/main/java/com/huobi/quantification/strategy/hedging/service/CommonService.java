package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dao.QuanContractCodeMapper;
import com.huobi.quantification.dao.StrategyHedgingConfigMapper;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.entity.StrategyHedgingConfig;
import com.huobi.quantification.strategy.hedging.config.ExchangeConfigHolder;
import com.huobi.quantification.strategy.hedging.entity.StartHedgingParam;

@Service
public class CommonService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	AccountInfoService accountInfoService;

	@Autowired
	QuanAccountFuturePositionService quanAccountFuturePositionService;

	@Autowired
	FutureContractService futureContractService;
	@Autowired
	private QuanContractCodeMapper quanContractCodeMapper;

	@Autowired
	StrategyHedgingConfigMapper strategyHedgingConfigMapper;
	@Autowired
	ExchangeConfigHolder exchangeConfigHolder;

	/**
	 * 计算当前的两个账户总的净头寸USDT
	 *
	 * @param startHedgingParam
	 * @return
	 */
	public BigDecimal calUSDTPosition(StartHedgingParam startHedgingParam) {

		// 2.1 获取火币现货账户期末USDT余额
		BigDecimal spotCurrentUSDT = accountInfoService.getHuobiSpotCurrentBalance(startHedgingParam.getSpotAccountID(),
				startHedgingParam.getSpotExchangeId(), startHedgingParam.getQuoteCoin());
		// 2.2获取火币现货账户期初USDT余额 策略启动时计算
		BigDecimal spotInitUSDT = startHedgingParam.getSpotInitUSDT();

		// 2.3 获取火币期货账户期末净空仓金额USD
		BigDecimal futureCurrentUSD = new BigDecimal(0);
		futureCurrentUSD = accountInfoService.getFutureUSDPosition(startHedgingParam.getFutureAccountID(),
				startHedgingParam.getFutureExchangeId(), startHedgingParam.getQuoteCoin());

		// 2.4 获取火币期货账户期初USD余额 策略启动时计算
		BigDecimal futureInitUSD = startHedgingParam.getFutureInitUSD();

		// 2.5 获取USDT USD的汇率
		BigDecimal rateOfUSDT2USD = getExchangeRateOfUSDT2USD();

		// 2.6 计算净头寸
		BigDecimal spotTotal = spotCurrentUSDT.subtract(spotInitUSDT);
		BigDecimal furureTotal = (futureCurrentUSD.subtract(futureInitUSD)).multiply(rateOfUSDT2USD);
		BigDecimal total = spotTotal.subtract(furureTotal);
		// 暂时只买一半
		logger.info("现货期初金额{},现货期末金额{}.期货期初金额{},期货期末金额{},利率为{}", spotInitUSDT, spotCurrentUSDT, futureInitUSD,
				futureCurrentUSD, rateOfUSDT2USD);
		return total.divide(BigDecimal.valueOf(10), 18, BigDecimal.ROUND_DOWN);// 缩小十倍下单
	}

	/**
	 * 获取USDT/USD利率
	 *
	 * @return
	 */
	public BigDecimal getExchangeRateOfUSDT2USD() {
		for (int i = 0; i < 3; i++) {
			try {
				BigDecimal rateResult = futureContractService.getExchangeRateOfUSDT2USD().getData();
				if (null == rateResult) {
					continue;
				}
				return rateResult;
			} catch (Exception e) {
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取对冲配置信息
	 *
	 * @param exchangeId
	 * @param contractCode
	 * @return
	 */
	/**
	 * 获取对冲配置信息
	 * 
	 * @param exchangeId
	 * @param contractCode
	 * @return
	 */
	public StrategyHedgingConfig getStrategyHedgingConfig(int exchangeId, String contractCode, String coin) {
		logger.info("查询对冲配置信息的条件为exchangeId:{} |contractCode:{} |  coin:{} ", exchangeId, contractCode, coin);
		QuanContractCode quanContractCode = quanContractCodeMapper.selectContractCodeByCode(exchangeId, contractCode);
		StrategyHedgingConfig result = strategyHedgingConfigMapper.selectStrategyHedging(coin,
				quanContractCode.getContractType());
		return result;
	}
	
	public boolean checkPlaceOrderInfo(SpotPlaceOrderReqDto reqDto){
		QuanExchangeConfig quanExchangeConfig=exchangeConfigHolder.getExchangeConfigBySymbol(reqDto.getBaseCoin()+reqDto.getQuoteCoin());
		BigDecimal quantity = new BigDecimal(0.00);// 数量
		BigDecimal price = new BigDecimal(0.00); // 下单价格
		quantity=reqDto.getQuantity().setScale(quanExchangeConfig.getAmountPrecision(), RoundingMode.DOWN);
		price=reqDto.getPrice().setScale(quanExchangeConfig.getPricePrecision(),  RoundingMode.DOWN);
		if(BigDecimalUtils.equals(quantity, BigDecimal.ZERO)||BigDecimalUtils.equals(price, BigDecimal.ZERO)) {
			logger.info("数量太小/金额太小，不下单");
			return false;
		}
		reqDto.setPrice(price);
		reqDto.setQuantity(quantity);
		return true;
		
	}

}
