package com.huobi.quantification.strategy.risk.enums;

/**
 * 摆单状态：	0：正常运行 
 * 		  	1：停止下开仓单，只下平仓单 
 * 			2：停止合约摆盘，撤销合约账户所有未成交订单
 * @author lichenyang
 * @since  2018年7月27日
 */
public enum RiskOrderTypeEnum {
	
	NORMAL_ORDER(0),ONLY_CLOSE_ORDER(1),STOP_ORDER(2);
	
	private int orderType;
	
	private RiskOrderTypeEnum(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderType() {
		return orderType;
	}

}
