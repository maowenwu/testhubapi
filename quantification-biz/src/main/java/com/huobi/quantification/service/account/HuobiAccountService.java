package com.huobi.quantification.service.account;

import java.util.List;

import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountSecret;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiAccountService {

	public Object accounts(String accountId);

	void updateAccount(String accountId);

	public List<Long> findAccountByExchangeId(int exId);

	public List<QuanAccountSecret> findAccountSecretById(Long accountId);
}
