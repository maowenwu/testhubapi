package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FutureBalanceRespDto.DataBean;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.risk.entity.FutureBalance;
import com.huobi.quantification.strategy.risk.entity.SpotBalance;
import com.huobi.quantification.strategy.risk.enums.RechargeTypeEnum;
import com.huobi.quantification.strategy.risk.enums.RiskHedgingTypeEnum;
import com.huobi.quantification.strategy.risk.enums.RiskOrderTypeEnum;

/**
 * @author lichenyang
 * @since  2018年7月26日
 */
@Component
public class QuantificationRiskManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StrategyRiskConfigMapper strategyRiskMapper;
	
	@Autowired
	private FutureAccountService futureAccountService;
	
	@Autowired
	private SpotAccountService spotAccountService;
	
	@Autowired
	private SpotOrderService spotOrderService;
	
	@Autowired
	private SpotMarketService spotMarketService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private QuanAccountHistoryMapper quanAccountHistoryMapper;
	
	/**
	 * 用于监控合约账户的保证金率，当低于限制1执行A方法，当低于限制2执行B方法，低于限制3执行C方法
	 * @param obj
	 */
	public void marginRateManage() {
		//根据contractCode获取合约账户的accountId
		String contractCode = "";
		
		//获取该合约账户的对应币种的保证金率
		BigDecimal marginRate = getMarginRate(contractCode);
		//获取数据库的保证金率阈值A,B,C，并进行判断
		StrategyRiskConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal marginRateLimit1 = riskManage.getMarginRateA();
		BigDecimal marginRateLimit2 = riskManage.getMarginRateB();
		BigDecimal marginRateLimit3 = riskManage.getMarginRateC();
		if (marginRate.compareTo(marginRateLimit3) < 0) {
			marginRateMethodC(contractCode, marginRateLimit2, marginRateLimit3);
		}else if (marginRate.compareTo(marginRateLimit2) < 0) {
			marginRateMethodB(contractCode, marginRateLimit2);
		}else if (marginRate.compareTo(marginRateLimit1) < 0) {
			marginRateMethodA(contractCode);
		}else {
			//当摆单状态为停止下开仓单，只下平仓单的话，将摆盘状态设为正常运行
			if (riskManage.getOrderRiskManagement() == RiskOrderTypeEnum.ONLY_CLOSE_ORDER.getOrderType()) {
				strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.NORMAL_ORDER.getOrderType());
			}
		}
	}
	
	/**
	 * 根据contractCode获取对应coin的保证金率
	 * @param contractCode
	 * @return
	 */
	private BigDecimal getMarginRate(String contractCode) {
		FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
		long accountId = 1234;
		String coinType ="btc";
		long timeout = 60 * 1000;
		long maxDelay = 60 * 60 * 1000;
		balanceReqDto.setAccountId(accountId);
		balanceReqDto.setCoinType(coinType);
		balanceReqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
		balanceReqDto.setMaxDelay(maxDelay);
		balanceReqDto.setTimeout(timeout);
		ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(balanceReqDto);
		if (balance.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
			new RuntimeException("取不到期货账户余额，错误信息："+ balance.getMessage());
		}
		FutureBalanceRespDto data = balance.getData();
		Map<String, DataBean> data2 = data.getData();
		DataBean dataBean = data2.get(coinType);
		return dataBean.getRiskRate();
	}

	/**
	 * 当保证金率低于限制1时，停止下开仓单，只下平仓单
	 */
	private void marginRateMethodA(String contractCode) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.ONLY_CLOSE_ORDER.getOrderType());
	}
	
	/**
	 * 当保证金率低于2时，停止下开仓单，只下平仓单，并发出警报
	 */
	private void marginRateMethodB(String contractCode, BigDecimal marginRateLimit) {
		logger.info("警报：保证金率低于阈值:{},[contractCode={}]", marginRateLimit, contractCode);
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.ONLY_CLOSE_ORDER.getOrderType());
	}
	
	/**
	 * 当保证金率低于3时，停止下开仓单，只下平仓单，并发出警报，强制平掉部分仓位，直至保证金率恢复正常。
	 */
	private void marginRateMethodC(String contractCode, BigDecimal marginRateLimit1 , BigDecimal marginRateLimit2) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.ONLY_CLOSE_ORDER.getOrderType());
		logger.info("警报：保证金率低于阈值:{},[contractCode={}]", marginRateLimit2, contractCode);
		//强制平掉部分仓位
	}
	
	/**
	 * 盈亏监控
	 * 分为：本次盈亏（和流动性项目策略开始启动时比较）和总盈亏（和最初的持有量比较）
	 */
	public void profitLossRiskManage() {
		//根据contractCode取出accountId,并计算得出总盈利和单次盈利
		String contractCode = "";
		BigDecimal OnceProfitLoss = getOnceProfitLoss(contractCode);
		BigDecimal TotalProfitLoss = getTotalProfitLoss(contractCode);
		//监控总盈利和单次盈利
		StrategyRiskConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal onceProfitLossA = riskManage.getOnceProfitLossA();
		BigDecimal onceProfitLossB = riskManage.getOnceProfitLossB();
		BigDecimal totalProfitLossA = riskManage.getTotalProfitLossA();
		BigDecimal totalProfitLossB = riskManage.getTotalProfitLossB();
		boolean index = false;
		if (OnceProfitLoss.compareTo(onceProfitLossB) > 0) {
			profitLossMethodB(onceProfitLossB, contractCode);
			index = true;
		}else if (OnceProfitLoss.compareTo(onceProfitLossA) > 0) {
			profitLossMethodA(onceProfitLossA, contractCode);
		}
		if (TotalProfitLoss.compareTo(totalProfitLossB) > 0) {
			profitLossMethodD(totalProfitLossB, contractCode);
			index = true;
		}else if (TotalProfitLoss.compareTo(totalProfitLossA) > 0) {
			profitLossMethodC(totalProfitLossA, contractCode);
		}
		if (index && riskManage.getOrderRiskManagement() == RiskOrderTypeEnum.STOP_ORDER_PROFIT.getOrderType()) {
			strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.NORMAL_ORDER.getOrderType());
		}
		if (index && riskManage.getHedgingRiskManagement() == RiskHedgingTypeEnum.STOP_HADGING_PROFIT.getHadgingType()) {
			strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.NORMAL_HADGING.getHadgingType());
		}
	}
	
	/**
	 * 获得总盈亏
	 * @param contractCode
	 * @return
	 */
    private BigDecimal getTotalProfitLoss(String contractCode) {
    	//取期货账户信息
    	FutureBalance start = new FutureBalance();
    	String coin1 = "usdt";
		String coin2 = "btc";
		FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
		long accountId = 1234;
		long futureAccountId = 456;
		long timeout = 60 * 1000;
		long maxDelay = 60 * 1000;
		balanceReqDto.setAccountId(futureAccountId);
		balanceReqDto.setCoinType(coin2);
		balanceReqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
		balanceReqDto.setMaxDelay(maxDelay);
		balanceReqDto.setTimeout(timeout);
    	ServiceResult<FutureBalanceRespDto> end = futureAccountService.getBalance(balanceReqDto);
    	if (end.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
			new RuntimeException("取不到期货用户当前余额，错误信息:" + end.getMessage());
		}
    	FutureBalance endFuture = new FutureBalance();
		endFuture.setMarginBalance(end.getData().getData().get(coin2).getMarginBalance());
		//取现货账户信息
		SpotBalanceReqDto balanceReqDto2 = new SpotBalanceReqDto();
		balanceReqDto2.setAccountId(accountId);
		balanceReqDto2.setExchangeId(ExchangeEnum.HUOBI.getExId());
		balanceReqDto2.setMaxDelay(maxDelay);
		balanceReqDto2.setTimeout(timeout);
		ServiceResult<SpotBalanceRespDto> end2 = spotAccountService.getBalance(balanceReqDto2);
		if (end2.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
			new RuntimeException("取不到现货用户当前余额，错误信息：" + end2.getMessage());
		}
		SpotBalance startSpotCoin1 = new SpotBalance();
		SpotBalance startSpotCoin2 = new SpotBalance();
		SpotBalance endSpotCoin1 = new SpotBalance();
		SpotBalance endSpotCoin2 = new SpotBalance();
		endSpotCoin1.setTotal(end2.getData().getData().get(coin1).getTotal());
		endSpotCoin2.setTotal(end2.getData().getData().get(coin2).getTotal());
		startSpotCoin1.setTotal(new BigDecimal(1000));
		startSpotCoin2.setTotal(new BigDecimal(1000));
		BigDecimal endDebitCoin1 = getEndDebit(coin1, ExchangeEnum.HUOBI.getExId(), accountId);
		BigDecimal endDebitcoin2 = getEndDebit(coin2, ExchangeEnum.HUOBI.getExId(), accountId);
		BigDecimal endDebitFuture = getEndDebit(coin2, ExchangeEnum.HUOBI_FUTURE.getExId(), futureAccountId);
		SpotCurrentPriceReqDto currentPriceReqDto = new SpotCurrentPriceReqDto();
		currentPriceReqDto.setBaseCoin(coin2);
		currentPriceReqDto.setQuoteCoin(coin1);
		currentPriceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		currentPriceReqDto.setMaxDelay(maxDelay);
		currentPriceReqDto.setTimeout(timeout);
		ServiceResult<SpotCurrentPriceRespDto> currentPriceResp = spotMarketService.getCurrentPrice(currentPriceReqDto);
		BigDecimal currentPrice = currentPriceResp.getData().getCurrentPrice();
		BigDecimal number1 = endSpotCoin1.getTotal().subtract(endDebitCoin1).subtract(startSpotCoin1.getTotal());
		BigDecimal number2 = endSpotCoin2.getTotal().subtract(endDebitcoin2).subtract(startSpotCoin2.getTotal().divide(currentPrice));
		BigDecimal number3 = endFuture.getMarginBalance().subtract(endDebitFuture).subtract(start.getMarginBalance());
		BigDecimal profitLoss = number1.add(number2).add(number3);
		return profitLoss;
    }
    
    /**
     * 获得本次运行盈亏
     * @param contractCode
     * @return
     */
	private BigDecimal getOnceProfitLoss(String contractCode) {
    	String coin1 = "usdt";
		String coin2 = "btc";
		FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
		long accountId = 1234;
		long futureAccountId = 12345;
		long timeout = 60 * 1000;
		long maxDelay = 60 * 1000;
		balanceReqDto.setAccountId(futureAccountId);
		balanceReqDto.setCoinType(coin2);
		balanceReqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
		balanceReqDto.setMaxDelay(maxDelay);
		balanceReqDto.setTimeout(timeout);
		ServiceResult<FutureBalanceRespDto> start = futureAccountService.getAccountInfo(futureAccountId, contractCode);
		ServiceResult<FutureBalanceRespDto> end = futureAccountService.getBalance(balanceReqDto);
		if (start.getCode() != ServiceErrorEnum.SUCCESS.getCode() || end.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
			String msg = null;
			if (start.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
				msg = start.getMessage();
			}else {
				msg = end.getMessage();
			}
			new RuntimeException("取不到期货用户余额，错误信息：" + msg);
		}
		FutureBalance startFuture = new FutureBalance();
		FutureBalance endFuture = new FutureBalance();
		startFuture.setMarginBalance(start.getData().getData().get(coin2).getMarginBalance());
		endFuture.setMarginBalance(end.getData().getData().get(coin2).getMarginBalance());
		SpotBalanceReqDto balanceReqDto2 = new SpotBalanceReqDto();
		balanceReqDto2.setAccountId(accountId);
		balanceReqDto2.setExchangeId(ExchangeEnum.HUOBI.getExId());
		balanceReqDto2.setMaxDelay(maxDelay);
		balanceReqDto2.setTimeout(timeout);
		ServiceResult<SpotBalanceRespDto> end2 = spotAccountService.getBalance(balanceReqDto2);
		if (end2.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
			new RuntimeException("取不到现货用户当前余额，错误信息：" + end2.getMessage());
		}
		List<QuanAccountAsset> firstBalance = spotAccountService.getFirstBalance(accountId, contractCode);
		SpotBalance startSpotCoin1 = new SpotBalance();
		SpotBalance startSpotCoin2 = new SpotBalance();
		SpotBalance endSpotCoin1 = new SpotBalance();
		SpotBalance endSpotCoin2 = new SpotBalance();
		for (QuanAccountAsset quanAccountAsset : firstBalance) {
			if (coin1.equals(quanAccountAsset.getCoin())) {
				startSpotCoin1.setTotal(quanAccountAsset.getTotal());
			}
			if (coin2.equals(quanAccountAsset.getCoin())) {
				startSpotCoin2.setTotal(quanAccountAsset.getTotal());
			}
		}
		endSpotCoin1.setTotal(end2.getData().getData().get(coin1).getTotal());
		endSpotCoin2.setTotal(end2.getData().getData().get(coin2).getTotal());
		//获取币币账户的两个币种的期末净借贷和合约用户的期末净借贷
		BigDecimal endDebitCoin1 = getEndDebit(coin1, ExchangeEnum.HUOBI.getExId(), accountId);
		BigDecimal endDebitcoin2 = getEndDebit(coin2, ExchangeEnum.HUOBI.getExId(), accountId);
		BigDecimal endDebitFuture = getEndDebit(coin2, ExchangeEnum.HUOBI_FUTURE.getExId(), futureAccountId);
		//获取币币账户的两个币种的期初净借贷和合约用户的期初净借贷
		Map<String, BigDecimal> startDebit = getStartDebit();
		BigDecimal startDebitCoin1 = startDebit.get(coin1+"_"+ ExchangeEnum.HUOBI.getExId()+"_"+ accountId);
		BigDecimal startDebitCoin2 = startDebit.get(coin2+"_"+ ExchangeEnum.HUOBI.getExId()+"_"+ accountId);
		BigDecimal startDebitFuture = startDebit.get(coin2+"_"+ ExchangeEnum.HUOBI_FUTURE.getExId()+"_"+ futureAccountId);
		//获取币币交易最新成交价
		SpotCurrentPriceReqDto currentPriceReqDto = new SpotCurrentPriceReqDto();
		currentPriceReqDto.setBaseCoin(coin2);
		currentPriceReqDto.setQuoteCoin(coin1);
		currentPriceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		currentPriceReqDto.setMaxDelay(maxDelay);
		currentPriceReqDto.setTimeout(timeout);
		ServiceResult<SpotCurrentPriceRespDto> currentPriceResp = spotMarketService.getCurrentPrice(currentPriceReqDto);
		BigDecimal currentPrice = currentPriceResp.getData().getCurrentPrice();
		BigDecimal number1 = endSpotCoin1.getTotal().subtract(endDebitCoin1).
				subtract(startSpotCoin1.getTotal().subtract(startDebitCoin1));
		BigDecimal number2 = endSpotCoin2.getTotal().subtract(endDebitcoin2).
				subtract(startSpotCoin2.getTotal().subtract(startDebitCoin2).divide(currentPrice));
		BigDecimal number3 = endFuture.getMarginBalance().subtract(endDebitFuture).
				subtract(startFuture.getMarginBalance().subtract(startDebitFuture));
		BigDecimal profitLoss = number1.add(number2).add(number3);
		return profitLoss;
	}
	
	/**
	 * 
	 */
	private Map<String, BigDecimal> getStartDebit() {
		Map<String, BigDecimal> firstDebit = spotAccountService.getFirstDebit();
		return firstDebit;
	}

	/**
	 * 获取当前币种的净借贷
	 * @param coin1
	 * @param exId
	 * @param accountId
	 * @return
	 */
	private BigDecimal getEndDebit(String coin, int exchangeId, long accountId) {
		BigDecimal borrow = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin, RechargeTypeEnum.BORROW.getRechargeType());
		BigDecimal payment = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin, RechargeTypeEnum.REPAYMENT.getRechargeType());
		return borrow.subtract(payment);
	}

	/**
     * 盈亏处理方法A：当本次次运行亏损大于阈值1时，会发出警报
     */
	private void profitLossMethodA(BigDecimal onceProfitLoss, String contractCode) {
		logger.info("警报：本次盈利亏损大于阈值:{},[contractCode={}]", onceProfitLoss, contractCode);
	}
	
	/**
	 * 盈亏处理方法B：当本次运行亏损大于阈值2时，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 */
	private void profitLossMethodB(BigDecimal onceProfitLoss, String contractCode) {
		//停止合约摆盘，撤销合约订单
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER_PROFIT.getOrderType());
		//停止对冲程序，撤销对冲订单
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING_PROFIT.getHadgingType());
		cancelPositionOrder(contractCode);
		logger.info("警报：本次盈利亏损大于阈值:{},[contractCode={}]", onceProfitLoss, contractCode);
		
	}
	
	/**
	 * 盈亏处理方法C：当总盈亏大于阈值3时，会发出警报
	 */
	private void profitLossMethodC(BigDecimal totalProfitLoss, String contractCode) {
		logger.info("警报：总盈利亏损大于阈值:{},[contractCode={}]", totalProfitLoss, contractCode);
		
	}
	
	/**
	 * 盈亏处理方法D：当总盈亏大于阈值4时，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 */
	private void profitLossMethodD(BigDecimal totalProfitLoss, String contractCode) {
		//停止合约摆盘，撤销合约订单
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER_PROFIT.getOrderType());
		//停止对冲程序，撤销对冲订单
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING_PROFIT.getHadgingType());
		
		logger.info("警报：总盈利盈利亏损大于阈值:{},[contractCode={}]", totalProfitLoss, contractCode);
	}

	/**
	 * 净头寸监控(合约+币币账户组)：	1，如果净头寸的绝对值大于阈值1，会停止合约摆盘，撤销合约账户所有未成交订单，对冲程序继续执行，直至低于阈值，重新恢复合约摆盘；
	 * 						2，如果净头寸的绝对值大于阈值2，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报。
	 */
	public void positionRiskManage() {
		//通过接口获取当前账户组净头寸
		String contractCode = "";
		BigDecimal position = getCurrentPosition(contractCode);
		//获取数据库的净头寸阈值A,B
		StrategyRiskConfig riskMange = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal positionA = riskMange.getPositionA();
		BigDecimal positionB = riskMange.getPositionB();
		if (position.compareTo(positionB) > 0) {
			positionMethodB(positionB, contractCode);
		}else if (position.compareTo(positionA) > 0) {
			positionMethodA(positionA, contractCode);
		}else {
			//如果摆单和对冲模块状态都为对冲模块时，将摆单模块和对冲模块设为正常运行
			if (riskMange.getHedgingRiskManagement() == RiskHedgingTypeEnum.STOP_HADGING_POSITION.getHadgingType()) {
				strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.NORMAL_HADGING.getHadgingType());
			}
			if (riskMange.getOrderRiskManagement() == RiskOrderTypeEnum.STOP_ORDER_POSITION.getOrderType()) {
				strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.NORMAL_ORDER.getOrderType());
				
			}
		}
	}

	/**
	 * 如果净头寸的绝对值大于阈值1，会停止合约摆盘，撤销合约账户所有未成交订单，对冲程序继续执行，直至低于阈值，重新恢复合约摆盘
	 * @param positionA
	 * @param contractCode
	 */
	private void positionMethodA(BigDecimal positionA, String contractCode) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER_POSITION.getOrderType());
		logger.info("警报：净头寸绝对值大于阈值：{},[contractType={}]", positionA ,contractCode);
	}

	/**
	 * 如果净头寸的绝对值大于阈值2，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 * @param positionB
	 * @param contractCode
	 */
	private void positionMethodB(BigDecimal positionB, String contractCode) {
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING_POSITION.getHadgingType());
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER_PROFIT.getOrderType());
		//撤销对冲订单
		cancelPositionOrder(contractCode);
		logger.info("警报：净头寸绝对值大于阈值：{},[contractType={}]", positionB ,contractCode);
	}
	
	/**
	 * 用于取消对冲的订单
	 */
	private void cancelPositionOrder(String contractCode) {
		//根据contractCode查询用户信息
		Long accountId = 123l;
		String symbol = "btcusdt";
		//调用撤销订单接口方法
		boolean index = true;
		while (index) {
			//撤销订单，当返回数量等于100时继续执行，小于100时停止撤销订单
			ServiceResult<Object> cancelOrder = spotOrderService.cancelOrder(accountId, symbol , "" , 100);
			if (cancelOrder.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
				new RuntimeException("批量撤销订单失败，错误信息：" + cancelOrder.getCode());
			}
			String body = (String)cancelOrder.getData();
			JSONObject parseObject = JSON.parseObject(body);
			JSONObject jsonObject = parseObject.getJSONObject("data");
			Integer successCount = jsonObject.getInteger("success-count");
			Integer failedCount = jsonObject.getInteger("failed-count");
			Integer total = successCount + failedCount;
			if (total < 100) {
				index = false;	
			}
		}
	}

	/**
	 * 调用接口方法获取当前账号组净头寸，并返回
	 * @param contractCode
	 * @return
	 */
	private BigDecimal getCurrentPosition(String contractCode) {
		String coin1 = "usdt";
		String coin2 = "btc";
		long spotAccountId = 123;
		long futureAccountId = 456;
	 	StartHedgingParam startHedgingParam = new StartHedgingParam();
	 	startHedgingParam.setBaseCoin(coin1);
	 	startHedgingParam.setQuoteCoin(coin2);
	 	startHedgingParam.setFutureAccountID(futureAccountId);
	 	startHedgingParam.setContractCode(contractCode);
	 	startHedgingParam.setFutureExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
	 	startHedgingParam.setSpotAccountID(spotAccountId);
	 	startHedgingParam.setSpotExchangeId(ExchangeEnum.HUOBI.getExId());
	 	BigDecimal calUSDTPosition = commonService.calUSDTPosition(startHedgingParam);
		return calUSDTPosition;
	}
}
