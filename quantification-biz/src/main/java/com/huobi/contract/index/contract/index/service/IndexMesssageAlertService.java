package com.huobi.contract.index.contract.index.service;

public interface IndexMesssageAlertService {
	
	void smsRetryTask();
	
	void smsWaitingTask();
	
	void emailTask();

}


