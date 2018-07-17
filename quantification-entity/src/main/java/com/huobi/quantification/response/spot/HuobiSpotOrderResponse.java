package com.huobi.quantification.response.spot;

import com.alibaba.fastjson.annotation.JSONField;

public class HuobiSpotOrderResponse {
	private String status;
	
	@JSONField(name="data")
	private Long orderId;
	
	@JSONField(name="err-code")
	private String errorCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
