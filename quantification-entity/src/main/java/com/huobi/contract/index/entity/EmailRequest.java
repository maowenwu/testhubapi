package com.huobi.contract.index.entity;


public class EmailRequest {
	
    private String status;
    
    private Long emailId ;
    
	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
