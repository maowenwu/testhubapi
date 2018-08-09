package cn.huobi.framework.model;

import java.math.BigDecimal;

public class TradeFee {
	 /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer id;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String symbol;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String contractType;

    /**
     * 币币交易手续费率
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal spotFee;

    /**
     * 合约交易手续费率(此处填合约交易taker手续费率)
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal contractFee;

    /**
     * 合约交割手续费率
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal deliveryFee;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String createTime;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getSpotFee() {
		return spotFee;
	}

	public void setSpotFee(BigDecimal spotFee) {
		this.spotFee = spotFee;
	}

	public BigDecimal getContractFee() {
		return contractFee;
	}

	public void setContractFee(BigDecimal contractFee) {
		this.contractFee = contractFee;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
