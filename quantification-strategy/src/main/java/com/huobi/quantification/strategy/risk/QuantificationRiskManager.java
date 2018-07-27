package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
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
	private StrategyRiskManagementConfigMapper strategyRiskMapper;
	
	@Autowired
	private FutureAccountService futureAccountService;
	
	@Autowired
	private SpotAccountService spotAccountService;
	
	@Autowired
	private QuanAccountFutureMapper quanAccountFutureMapper;
	
	@Autowired
	private QuanAccountMapper quanAccountMapper;
	
	
	/**
	 * 用于监控合约账户的保证金率，当低于限制1执行A方法，当低于限制2执行B方法，低于限制3执行C方法
	 * @param obj
	 */
	public void marginRateManage() {
		//根据contractCode获取合约账户的accountId
		String contractCode = "";
		
		//获取该合约账户的对应币种的保证金率
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
		//获取数据库的保证金率阈值A,B,C，并进行判断
		StrategyRiskManagementConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
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
			//将摆盘状态设为0
			strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.NORMAL_ORDER.getOrderType());
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
		marginRateMethodA(contractCode);
	}
	
	/**
	 * 当保证金率低于3时，停止下开仓单，只下平仓单，并发出警报，强制平掉部分仓位，直至保证金率恢复正常。
	 */
	private void marginRateMethodC(String contractCode, BigDecimal marginRateLimit1 , BigDecimal marginRateLimit2) {
		logger.info("警报：保证金率低于阈值:{},[contractCode={}]", marginRateLimit2, contractCode);
		marginRateMethodB(contractCode, marginRateLimit1);
	}
	
	/**
	 * 盈亏监控
	 * 分为：本次盈亏（和流动性项目策略开始启动时比较）和总盈亏（和最初的持有量比较）
	 */
	public void profitAndLossRiskManage() {
		//根据contractCode取出accountId
		String contractCode = "";
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
		BigDecimal OnceProfitLoss = simpleCountProfitAndLoss(startFuture, endFuture, startSpotCoin1, endSpotCoin1, startSpotCoin2, endSpotCoin2);
		BigDecimal TotalProfitLoss = new BigDecimal("0.1");
		StrategyRiskManagementConfig riskManage = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal onceProfitLossA = riskManage.getOnceProfitLossA();
		BigDecimal onceProfitLossB = riskManage.getOnceProfitLossB();
		BigDecimal totalProfitLossA = riskManage.getTotalProfitLossA();
		BigDecimal totalProfitLossB = riskManage.getTotalProfitLossB();
		if (OnceProfitLoss.compareTo(onceProfitLossB) > 0) {
			profitLossMethodB(onceProfitLossB, contractCode);
		}else if (OnceProfitLoss.compareTo(onceProfitLossA) > 0) {
			profitLossMethodA(onceProfitLossA, contractCode);
		}
		if (TotalProfitLoss.compareTo(totalProfitLossB) > 0) {
			profitLossMethodD(totalProfitLossB, contractCode);
		}else if (TotalProfitLoss.compareTo(totalProfitLossA) > 0) {
			profitLossMethodC(totalProfitLossA, contractCode);
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
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER.getOrderType());
		//停止对冲程序，撤销对冲订单
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING.getHadgingType());
		
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
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER.getOrderType());
		//停止对冲程序，撤销对冲订单
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING.getHadgingType());
		
		logger.info("警报：总盈利盈利亏损大于阈值:{},[contractCode={}]", totalProfitLoss, contractCode);
	}

	/**
	 * 期末盈亏（BTC）简单算法：
	 * =（币币账户期末余额BTC  - 币币账户期初余额BTC）
	 * +（币币账户期末余额USDT - 币币账户期初余额USDT）/币币现货最新价 
	 * +（合约账户期末权益BTC - 合约账户期初权益BTC）
	 * @return
	 */
	private BigDecimal simpleCountProfitAndLoss(FutureBalance startFuture, FutureBalance endFuture, SpotBalance startSpotCoin1, SpotBalance endSpotCoin1,SpotBalance startSpotCoin2, SpotBalance endSpotCoin2 ) {
		BigDecimal number1 = endSpotCoin1.getAvailable().subtract(startSpotCoin1.getAvailable());
		BigDecimal number2 = endSpotCoin2.getAvailable().subtract(startSpotCoin2.getAvailable());
		BigDecimal number3 = endFuture.getMarginBalance().subtract(startFuture.getMarginBalance());
		BigDecimal profitLoss = number1.add(number2).add(number3);
		return profitLoss;
	}
	
	/**
	 * 期末盈亏（BTC）：
	 * =（币币账户期末余额BTC  - 币币账户期初余额BTC）
	 * +（币币账户期末余额USDT - 币币账户期初余额USDT）/币币现货最新价 
	 * +（合约账户期末静态权益BTC - 合约账户期初静态权益BTC）
	 * +（合约账户期末调整未实现盈亏BTC-合约账户期初调整未实现盈亏BTC）
	 * 
	 * 其中，余额：
	 * = 可用+冻结
	 * 
	 * 合约账户静态权益：
	 * = 合约账户权益 – 合约账户未实现盈亏
	 * = 合约账户余额 + 合约账户已实现盈亏
	 * 
	 * 合约账户调整未实现盈亏：
	 * =[1/多仓持仓均价 – 1/(币币现货最新成交价*汇率)]*多仓持仓张数 *单张合约面值
	 * +[1/(币币现货最新成交价*汇率) – 1/空仓持仓均价]*空仓持仓张数 *单张合约面值
	 * @return
	 */
	private BigDecimal complexCountProfitAndLoss() {
		
		return null;
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
		StrategyRiskManagementConfig riskMange = strategyRiskMapper.selectByContractCode(contractCode);
		BigDecimal positionA = riskMange.getPositionA();
		BigDecimal positionB = riskMange.getPositionB();
		if (position.compareTo(positionB) > 0) {
			positionMethodB(positionB, contractCode);
		}else if (position.compareTo(positionA) > 0) {
			positionMethodA(positionA, contractCode);
		}else {
			//将摆单模块和对冲模块设为正常运行
			strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.NORMAL_HADGING.getHadgingType());
			strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.NORMAL_ORDER.getOrderType());
		}
	}

	/**
	 * 如果净头寸的绝对值大于阈值1，会停止合约摆盘，撤销合约账户所有未成交订单，对冲程序继续执行，直至低于阈值，重新恢复合约摆盘
	 * @param positionA
	 * @param contractCode
	 */
	private void positionMethodA(BigDecimal positionA, String contractCode) {
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER.getOrderType());
		logger.info("警报：净头寸绝对值大于阈值：{},[contractType={}]", positionA ,contractCode);
	}

	/**
	 * 如果净头寸的绝对值大于阈值2，会停止合约摆盘， 停止对冲程序，撤销两账户所有未成交订单，并发出警报
	 * @param positionB
	 * @param contractCode
	 */
	private void positionMethodB(BigDecimal positionB, String contractCode) {
		strategyRiskMapper.updateHedgingRiskManagement(contractCode, RiskHedgingTypeEnum.STOP_HADGING.getHadgingType());
		strategyRiskMapper.updateOrderRiskManagement(contractCode, RiskOrderTypeEnum.STOP_ORDER.getOrderType());
		//撤销对冲订单
		
		logger.info("警报：净头寸绝对值大于阈值：{},[contractType={}]", positionB ,contractCode);
	}
	
	/**
	 * 调用接口方法获取当前账号组净头寸，并返回
	 * @param contractCode
	 * @return
	 */
	private BigDecimal getCurrentPosition(String contractCode) {
		return null;
	}
}
