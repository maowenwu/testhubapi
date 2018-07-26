package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.dao.StrategyRiskManagementConfigMapper;
import com.huobi.quantification.entity.StrategyRiskManagementConfig;

/**
 * @author lichenyang
 * @since  2018年7月26日
 */
@Component
public class QuantificationRiskManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StrategyRiskManagementConfigMapper strategyRiskMapper;
	
	/**
	 * 用于监控合约账户的保证金率，当低于限制1执行A方法，当低于限制2执行B方法，低于限制3执行C方法
	 * @param obj
	 */
	public void marginRateManage(BigDecimal marginRate) {
		String contractCode = "";
		StrategyRiskManagementConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal marginRateLimit1 = riskManage.getMarginRateA();
		BigDecimal marginRateLimit2 = riskManage.getMarginRateB();
		BigDecimal marginRateLimit3 = riskManage.getMarginRateC();
		if (marginRate.compareTo(marginRateLimit3) < 0) {
			marginRateMethodC();
		}else if (marginRate.compareTo(marginRateLimit2) < 0) {
			marginRateMethodB();
		}else if (marginRate.compareTo(marginRateLimit1) < 0) {
			marginRateMethodA();
		}
	}
	
	/**
	 * 当保证金率低于限制1时，停止下开仓单，只下平仓单
	 */
	private void marginRateMethodA() {
		
	}
	
	/**
	 * 当保证金率低于2时，停止下开仓单，只下平仓单，并发出警报
	 */
	private void marginRateMethodB() {
		marginRateMethodA();
	}
	
	/**
	 * 当保证金率低于3时，停止下开仓单，只下平仓单，并发出警报，强制平掉部分仓位，直至保证金率恢复正常。
	 */
	private void marginRateMethodC() {
		marginRateMethodB();
	}
	/**
	 * 盈亏监控
	 * 分为：本次盈亏（和流动性项目策略开始启动时比较）和总盈亏（和最初的持有量比较）
	 */
	public void profitAndLossRiskManage() {
		BigDecimal OnceProfitLoss = countProfitAndLoss();
		BigDecimal TotalProfitLoss = countProfitAndLoss();
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
	private BigDecimal countProfitAndLoss() {
		return null;
	}
}
