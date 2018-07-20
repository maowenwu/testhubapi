package com.huobi.quantification.dto;

import java.io.Serializable;

public class JobParamDto implements Serializable {
	private Long accountId;
	private String symbol;
	private String contractType;
	private String klineType;
	private String depthType;
	private Long orderId;
	private Integer size;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getKlineType() {
		return klineType;
	}

	public void setKlineType(String klineType) {
		this.klineType = klineType;
	}

	public String getDepthType() {
		return depthType;
	}

	public void setDepthType(String depthType) {
		this.depthType = depthType;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
