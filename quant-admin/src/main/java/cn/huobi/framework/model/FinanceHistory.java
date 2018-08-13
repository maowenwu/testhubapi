package cn.huobi.framework.model;

import java.math.BigDecimal;

public class FinanceHistory {
    /**
     * @mbg.generated 2018-08-03 15:13:17
     */
    private Long id;

    /**
     * 交易所id
     * @mbg.generated 2018-08-03 15:13:17
     */
    private String exchangeId;

    /**
     * 账号ID
     * @mbg.generated 2018-08-03 15:13:17
     */
    private Long accountId;

    /**
     * 币种
     * @mbg.generated 2018-08-03 15:13:17
     */
    private String coinType;

    /**
     * 充值提现金额
     * @mbg.generated 2018-08-03 15:13:17
     */
    private BigDecimal transferAmount;

    /**
     * 1.充值  2. 提现
     * @mbg.generated 2018-08-03 15:13:17
     */
    private String moneyType;

    /**
     * @mbg.generated 2018-08-03 15:13:17
     */
    private Integer init;

    /**
     * 创建时间
     * @mbg.generated 2018-08-03 15:13:17
     */
    private String createTime;

    /**
     * @mbg.generated 2018-08-03 15:13:17
     */
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Integer getInit() {
        return init;
    }

    public void setInit(Integer init) {
        this.init = init;
    }

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
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