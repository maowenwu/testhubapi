package com.huobi.quantification.strategy.risk.enums;

/**
 * 风控模块处理状态：	0：正常运行
 * 				1：停止对冲程序
 * @author lichenyang
 * @since  2018年7月27日
 */
public enum RiskHedgingTypeEnum {
	NORMAL_HADGING(0),STOP_HADGING(1);
	
	private int hadgingType;

	private RiskHedgingTypeEnum(int hadgingType) {
		this.hadgingType = hadgingType;
	}

	public int getHadgingType() {
		return hadgingType;
	}
}
