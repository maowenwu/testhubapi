package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyHedgingConfig {
	/**
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private Integer id;

	/**
	 * 币种
	 * 
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private String coin;

	/**
	 * 合约code
	 * 
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private String contractCode;

	/**
	 * 合约类型
	 * 
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private String contractType;

	/**
	 * 手续费率
	 * 
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private BigDecimal formalityRate;

	/**
	 * 滑点
	 * 
	 * @mbg.generated 2018-08-01 16:57:57
	 */
	private BigDecimal slippage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public BigDecimal getFormalityRate() {
		return formalityRate;
	}

	public void setFormalityRate(BigDecimal formalityRate) {
		this.formalityRate = formalityRate;
	}

	public BigDecimal getSlippage() {
		return slippage;
	}

	public void setSlippage(BigDecimal slippage) {
		this.slippage = slippage;
	}
}