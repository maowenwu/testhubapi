package com.huobi.quantification.service.account;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiAccountService {
	
	 public Object accounts(String accountId);
	 
	 void updateAccount(String accountId);
}
