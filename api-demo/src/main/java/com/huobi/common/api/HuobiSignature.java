package com.huobi.common.api;

public class HuobiSignature {
	private String accessKey;
	private String secretKey;
	
	public HuobiSignature() {
		super();
	}

	public HuobiSignature(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
