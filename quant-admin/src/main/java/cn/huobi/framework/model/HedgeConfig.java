package cn.huobi.framework.model;

import java.math.BigDecimal;

public class HedgeConfig {
    /**
     * @mbg.generated 2018-08-06 11:33:39
     */
    private Integer id;

    /**
     * 币种
     * @mbg.generated 2018-08-06 11:33:39
     */
    private String symbol;

    /**
     * 合约类型
     * @mbg.generated 2018-08-06 11:33:39
     */
    private String contractType;

    /**
     * 手续费率
     * @mbg.generated 2018-08-06 11:33:39
     */
    private BigDecimal spotFee;

    /**
     * 滑点
     * @mbg.generated 2018-08-06 11:33:39
     */
    private BigDecimal slippage;

    /**
     * 交割对冲周期
     * @mbg.generated 2018-08-06 11:33:39
     */
    private Integer placeOrderInterval;

    /**
     * @mbg.generated 2018-08-06 11:33:39
     */
    private String createTime;

    /**
     * @mbg.generated 2018-08-06 11:33:39
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

    public BigDecimal getSlippage() {
        return slippage;
    }

    public void setSlippage(BigDecimal slippage) {
        this.slippage = slippage;
    }

    public Integer getPlaceOrderInterval() {
        return placeOrderInterval;
    }

    public void setPlaceOrderInterval(Integer placeOrderInterval) {
        this.placeOrderInterval = placeOrderInterval;
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