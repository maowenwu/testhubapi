package com.huobi.quantification.strategy.risk.enums;

/**
 * 借款：1，还款：-1
 * @author lichenyang
 * @since  2018年7月31日
 */
public enum RechargeTypeEnum {
	BORROW(1),REPAYMENT(-1);
	
	private int rechargeType;

	private RechargeTypeEnum(int rechargeType) {
		this.rechargeType = rechargeType;
	}

	public int getRechargeType() {
		return rechargeType;
	}
}
