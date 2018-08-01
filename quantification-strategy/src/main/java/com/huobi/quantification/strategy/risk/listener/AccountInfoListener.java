package com.huobi.quantification.strategy.risk.listener;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.risk.enums.RechargeTypeEnum;

@Component
public class AccountInfoListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private FutureAccountService futureAccountService;

	@Autowired
	private SpotAccountService spotAccountService;
	
	@Autowired
	private QuanAccountHistoryMapper quanAccountHistoryMapper;

	@Autowired
	private QuanAccountMapper quanAccountMapper;

	@Autowired
	private QuanAccountFutureMapper quanAccountFutureMapper;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			//根据contractCode获取现货交易账户id和期货交易账户id
			long spotAccountId = 4295363l;
			long futureAccountId = 123l;
			String contractCode = "BTC0727";
			String coin1 = "usdt";
			String coin2 = "btc";
			try {
//			spotAccountService.saveFirstBalance(spotAccountId, contractCode);
//			futureAccountService.saveAccountsInfo(futureAccountId, contractCode);
//			BigDecimal spotDebitCoin1 = getEndDebit(coin1, ExchangeEnum.HUOBI.getExId(), spotAccountId);
//			BigDecimal spotDebitCoin2 = getEndDebit(coin2, ExchangeEnum.HUOBI.getExId(), spotAccountId);
//			BigDecimal futureDebit = getEndDebit(coin2, ExchangeEnum.HUOBI_FUTURE.getExId(), futureAccountId);
//			HashMap<String, BigDecimal> hashMap = new HashMap<>();
//			hashMap.put(coin1+"_"+ ExchangeEnum.HUOBI.getExId()+"_"+spotAccountId, spotDebitCoin1);
//			hashMap.put(coin2+"_"+ ExchangeEnum.HUOBI.getExId()+"_"+spotAccountId, spotDebitCoin2);
//			hashMap.put(coin2+"_"+ ExchangeEnum.HUOBI_FUTURE.getExId()+"_"+futureAccountId, futureDebit);
//			spotAccountService.saveFirstDebit(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private BigDecimal getEndDebit(String coin, int exchangeId, long accountId) {
		BigDecimal borrow = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin, RechargeTypeEnum.BORROW.getRechargeType());
		BigDecimal payment = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin, RechargeTypeEnum.REPAYMENT.getRechargeType());
		return borrow.subtract(payment);
	}

}
