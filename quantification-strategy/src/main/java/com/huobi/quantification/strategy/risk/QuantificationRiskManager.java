package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyRiskManagementConfigMapper;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FutureBalanceRespDto.DataBean;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.entity.StrategyRiskManagementConfig;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.risk.entity.FutureBalance;
import com.huobi.quantification.strategy.risk.entity.SpotBalance;

/**
 * @author lichenyang
 * @since  2018年7月26日
 */
@Component
public class QuantificationRiskManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StrategyRiskManagementConfigMapper strategyRiskMapper;
	
	@Autowired
	private FutureAccountService futureAccountService;
	
	@Autowired
	private SpotAccountService spotAccountService;
	/**
	 * 用于监控合约账户的保证金率，当低于限制1执行A方法，当低于限制2执行B方法，低于限制3执行C方法
	 * @param obj
	 */
	public void marginRateManage() {
		String contractCode = "";
		FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
		long accountId = 1234;
		String coinType ="btc";
		long timeout = 1000;
		long maxDelay = 3000;
		balanceReqDto.setAccountId(accountId);
		balanceReqDto.setCoinType(coinType);
		balanceReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
		balanceReqDto.setMaxDelay(maxDelay);
		balanceReqDto.setTimeout(timeout);
		ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(balanceReqDto);
		FutureBalanceRespDto data = balance.getData();
		Map<String, DataBean> data2 = data.getData();
		DataBean dataBean = data2.get("btc");
		BigDecimal marginRate = dataBean.getRiskRate();
		StrategyRiskManagementConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal marginRateLimit1 = riskManage.getMarginRateA();
		BigDecimal marginRateLimit2 = riskManage.getMarginRateB();
		BigDecimal marginRateLimit3 = riskManage.getMarginRateC();
		if (marginRate.compareTo(marginRateLimit3) < 0) {
			marginRateMethodC(contractCode);
		}else if (marginRate.compareTo(marginRateLimit2) < 0) {
			marginRateMethodB(contractCode);
		}else if (marginRate.compareTo(marginRateLimit1) < 0) {
			marginRateMethodA(contractCode);
		}
	}
	
	/**
	 * 当保证金率低于限制1时，停止下开仓单，只下平仓单
	 */
	private void marginRateMethodA(String contractCode) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, 1);
	}
	
	/**
	 * 当保证金率低于2时，停止下开仓单，只下平仓单，并发出警报
	 */
	private void marginRateMethodB(String contractCode) {
		logger.info("警报：保证金率低于阈值2");
		marginRateMethodA(contractCode);
	}
	
	/**
	 * 当保证金率低于3时，停止下开仓单，只下平仓单，并发出警报，强制平掉部分仓位，直至保证金率恢复正常。
	 */
	private void marginRateMethodC(String contractCode) {
		logger.info("警报：保证金率低于阈值3");
		marginRateMethodB(contractCode);
	}
	
	/**
	 * 盈亏监控
	 * 分为：本次盈亏（和流动性项目策略开始启动时比较）和总盈亏（和最初的持有量比较）
	 */
	public void profitAndLossRiskManage() {
		//取出期货权益对象(本次期货开始时对象，本次期货当前对象，期货初始对象)
		String coin1 = "usdt";
		String coin2 = "btc";
		FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
		long accountId = 1234;
		String coinType ="btc";
		long timeout = 1000;
		long maxDelay = 3000;
		balanceReqDto.setAccountId(accountId);
		balanceReqDto.setCoinType(coinType);
		balanceReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
		balanceReqDto.setMaxDelay(maxDelay);
		balanceReqDto.setTimeout(timeout);
		ServiceResult<FutureBalanceRespDto> start = futureAccountService.getBalance(balanceReqDto);
		ServiceResult<FutureBalanceRespDto> end = futureAccountService.getBalance(balanceReqDto);
		FutureBalance startFuture = new FutureBalance();
		FutureBalance endFuture = new FutureBalance();
		startFuture.setMarginBalance(start.getData().getData().get(coin2).getMarginBalance());
		endFuture.setMarginBalance(end.getData().getData().get(coin2).getMarginBalance());
		//取出现货权益对象
		SpotBalanceReqDto balanceReqDto2 = new SpotBalanceReqDto();
		balanceReqDto2.setAccountId(accountId);
		balanceReqDto2.setExchangeId(ExchangeEnum.HUOBI.getExId());
		balanceReqDto2.setMaxDelay(maxDelay);
		balanceReqDto2.setTimeout(timeout);
		ServiceResult<SpotBalanceRespDto> start2 = spotAccountService.getBalance(balanceReqDto2);
		ServiceResult<SpotBalanceRespDto> end2 = spotAccountService.getBalance(balanceReqDto2);
		SpotBalance startSpotCoin1 = new SpotBalance();
		SpotBalance startSpotCoin2 = new SpotBalance();
		SpotBalance endSpotCoin1 = new SpotBalance();
		SpotBalance endSpotCoin2 = new SpotBalance();
		startSpotCoin1.setAvailable(start2.getData().getData().get(coin1).getAvailable());
		startSpotCoin2.setAvailable(start2.getData().getData().get(coin2).getAvailable());
		endSpotCoin1.setAvailable(end2.getData().getData().get(coin1).getAvailable());
		endSpotCoin2.setAvailable(end2.getData().getData().get(coin2).getAvailable());
		//计算盈利
		BigDecimal OnceProfitLoss = countProfitAndLoss(startFuture, endFuture, startSpotCoin1, endSpotCoin1, startSpotCoin2, endSpotCoin2);
		BigDecimal TotalProfitLoss = new BigDecimal("0.1");
		String contractCode = "";
		StrategyRiskManagementConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal onceProfitLossA = riskManage.getOnceProfitLossA();
		BigDecimal onceProfitLossB = riskManage.getOnceProfitLossB();
		BigDecimal totalProfitLossA = riskManage.getTotalProfitLossA();
		BigDecimal totalProfitLossB = riskManage.getTotalProfitLossB();
		if (OnceProfitLoss.compareTo(onceProfitLossB) >= 0) {
			profitLossMethodB();
		}else if (OnceProfitLoss.compareTo(onceProfitLossA) >= 0) {
			profitLossMethodA();
		}
		if (TotalProfitLoss.compareTo(totalProfitLossB) >= 0) {
			profitLossMethodD();
		}else if (TotalProfitLoss.compareTo(totalProfitLossA) >= 0) {
			profitLossMethodC();
		}
	}
	
    /**
     * 盈亏处理方法A：当本次次运行亏损大于阈值1时，会发出警报
     */
	private void profitLossMethodA() {
		
	}
	
	/**
	 * 盈亏处理方法B：当本次运行亏损大于阈值2时，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 */
	private void profitLossMethodB() {
		
	}
	
	/**
	 * 盈亏处理方法C：当总盈亏大于阈值3时，会发出警报
	 */
	private void profitLossMethodC() {
		
	}
	
	/**
	 * 盈亏处理方法D：当总盈亏大于阈值4时，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 */
	private void profitLossMethodD() {
		
	}

	/**
	 * 期末盈亏（BTC）简单算法：
	 * =（币币账户期末余额BTC  - 币币账户期初余额BTC）
	 * +（币币账户期末余额USDT - 币币账户期初余额USDT）/币币现货最新价 
	 * +（合约账户期末权益BTC - 合约账户期初权益BTC）
	 * @return
	 */
	private BigDecimal countProfitAndLoss(FutureBalance startFuture, FutureBalance endFuture, SpotBalance startSpotCoin1, SpotBalance endSpotCoin1,SpotBalance startSpotCoin2, SpotBalance endSpotCoin2 ) {
		BigDecimal number1 = endSpotCoin1.getAvailable().subtract(startSpotCoin1.getAvailable());
		BigDecimal number2 = endSpotCoin2.getAvailable().subtract(startSpotCoin2.getAvailable());
		BigDecimal number3 = endFuture.getMarginBalance().subtract(startFuture.getMarginBalance());
		BigDecimal profitLoss = number1.add(number2).add(number3);
		return profitLoss;
	}
}
