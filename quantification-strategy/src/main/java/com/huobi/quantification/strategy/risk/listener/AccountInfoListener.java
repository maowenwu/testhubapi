package com.huobi.quantification.strategy.risk.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.dao.QuanAccountFutureMapper;
import com.huobi.quantification.dao.QuanAccountMapper;
import com.huobi.quantification.entity.QuanAccountFuture;

@Component
public class AccountInfoListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private FutureAccountService futureAccountService;

	@Autowired
	private SpotAccountService spotAccountService;

	@Autowired
	private QuanAccountMapper quanAccountMapper;

	@Autowired
	private QuanAccountFutureMapper quanAccountFutureMapper;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			//根据contractCode获取现货交易账户id和期货交易账户id
			long accountId = 4295363l;
			String contractCode = "BTC0727";
			spotAccountService.saveFirstBalance(accountId, contractCode);
			futureAccountService.saveAccountsInfo(accountId, contractCode);
		}
		
	}

}
