package com.huobi.contract.index.entity;

public enum ValidEnum {
	SUCC(1, "成功"), FAIL(0, "失败");
	private Integer status;
	private String msg;

	ValidEnum(Integer status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public Integer getStatus() {
		return status;
	}

	private void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	private void setMsg(String msg) {
		this.msg = msg;
	}

	public static ValidEnum getByStatus(Integer status) {
		for (ValidEnum valid : ValidEnum.values()) {
			if (valid.getStatus().equals(status)) {
				return valid;
			}
		}
		return null;
	}
}
