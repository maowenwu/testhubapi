package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
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
	private RiskContext riskContext;
	
	public void init(StrategyProperties.ConfigGroup group) {
		riskContext.init(group);
    }
	
	/**
	 * 用于监控合约账户的保证金率，当低于限制1执行A方法，当低于限制2执行B方法，低于限制3执行C方法
	 * @param obj
	 */
	public void marginRateManage(String contractCode) {
		//获取该合约账户的对应币种的保证金率
		BigDecimal marginRate = riskContext.getMarginRate();
		StrategyRiskConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal marginRateLimit1 = riskManage.getMarginRateA();
		BigDecimal marginRateLimit2 = riskManage.getMarginRateB();
		BigDecimal marginRateLimit3 = riskManage.getMarginRateC();
		if (marginRate.compareTo(marginRateLimit3) < 0) {
			marginRateMethodC(contractCode, marginRateLimit3);
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
	private void marginRateMethodC(String contractCode, BigDecimal marginRateLimit) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.ONLY_CLOSE_ORDER.getOrderType());
		logger.info("警报：保证金率低于阈值:{},[contractCode={}]", marginRateLimit, contractCode);
		//强制平掉部分仓位
		
	}
	
	/**
	 * 盈亏监控
	 * 分为：本次盈亏（和流动性项目策略开始启动时比较）和总盈亏（和最初的持有量比较）
	 */
	public void profitLossRiskManage(String contractCode) {
		//根据contractCode取出accountId,并计算得出总盈利和单次盈利
		BigDecimal OnceProfitLoss = riskContext.getOnceProfitLoss();
		BigDecimal TotalProfitLoss = riskContext.getTotalProfitLoss();
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
		riskContext.cancelPositionOrder();
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
	public void positionRiskManage(String contractCode) {
		//通过接口获取当前账户组净头寸
		BigDecimal position = riskContext.getCurrentPosition();
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
		riskContext.cancelPositionOrder();
		logger.info("警报：净头寸绝对值大于阈值：{},[contractType={}]", positionB ,contractCode);
	}
}
