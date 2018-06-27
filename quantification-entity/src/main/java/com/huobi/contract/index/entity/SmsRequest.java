package com.huobi.contract.index.entity;


public class SmsRequest {
	
    private String status;
    
    private Long smsId ;
    
    private int  dealTimes;	
    
	public Long getSmsId() {
		return smsId;
	}
	
	public void setSmsId(Long smsId) {
		this.smsId = smsId;
	}

	public int getDealTimes() {
		return dealTimes;
	}

	public void setDealTimes(int dealTimes) {
		this.dealTimes = dealTimes;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
