package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.dao.QuanAccountHistoryMapper;

@Service
public class QuanAccountFuturePositionService {

	@Autowired
	QuanAccountHistoryMapper quanAccountHistoryMapper;

	/**
	 * 获取指定账户指定币种的期初值
	 * @param accountId
	 * @param exchangeId
	 * @param accountsType
	 * @param coin
	 * @return
	 */
	public BigDecimal getInitAmount(Long accountId, int exchangeId, String accountsType, String coin) {
		BigDecimal result = quanAccountHistoryMapper.getInitAmount(accountId, exchangeId, accountsType, coin);
		return result;
	}

}
