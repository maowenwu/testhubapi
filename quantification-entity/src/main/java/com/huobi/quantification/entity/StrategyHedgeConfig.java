package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StrategyHedgeConfig {
    /**
     * @mbg.generated 2018-08-14 16:49:14
     */
    private Integer id;

    /**
     * 币种
     * @mbg.generated 2018-08-14 16:49:14
     */
    private String symbol;

    /**
     * 合约类型
     * @mbg.generated 2018-08-14 16:49:14
     */
    private String contractType;

    /**
     * 对冲间隔时间
     * @mbg.generated 2018-08-14 16:49:14
     */
    private Integer hedgeInterval;

    /**
     * 最小的对冲净头寸
     * @mbg.generated 2018-08-14 16:49:14
     */
    private BigDecimal minNetPosition;

    /**
     * 对冲买单滑点
     * @mbg.generated 2018-08-14 16:49:14
     */
    private BigDecimal buySlippage;

    /**
     * 对冲卖单滑点
     * @mbg.generated 2018-08-14 16:49:14
     */
    private BigDecimal sellSlippage;

    /**
     * 开始交割对冲时间
     * @mbg.generated 2018-08-14 16:49:14
     */
    private String stopTime1;

    /**
     * 结束交割对冲时间
     * @mbg.generated 2018-08-14 16:49:14
     */
    private String stopTime2;

    /**
     * 交割对冲单时间间隔
     * @mbg.generated 2018-08-14 16:49:14
     */
    private Integer deliveryInterval;

    /**
     * 交割买单滑点
     * @mbg.generated 2018-08-14 16:49:14
     */
    private BigDecimal deliveryBuySlippage;

    /**
     * 交割卖单滑点
     * @mbg.generated 2018-08-14 16:49:14
     */
    private BigDecimal deliverySellSlippage;

    /**
     * @mbg.generated 2018-08-14 16:49:14
     */
    private Date createTime;

    /**
     * @mbg.generated 2018-08-14 16:49:14
     */
    private Date updateTime;

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

    public Integer getHedgeInterval() {
        return hedgeInterval;
    }

    public void setHedgeInterval(Integer hedgeInterval) {
        this.hedgeInterval = hedgeInterval;
    }

    public BigDecimal getMinNetPosition() {
        return minNetPosition;
    }

    public void setMinNetPosition(BigDecimal minNetPosition) {
        this.minNetPosition = minNetPosition;
    }

    public BigDecimal getBuySlippage() {
        return buySlippage;
    }

    public void setBuySlippage(BigDecimal buySlippage) {
        this.buySlippage = buySlippage;
    }

    public BigDecimal getSellSlippage() {
        return sellSlippage;
    }

    public void setSellSlippage(BigDecimal sellSlippage) {
        this.sellSlippage = sellSlippage;
    }

    public String getStopTime1() {
        return stopTime1;
    }

    public void setStopTime1(String stopTime1) {
        this.stopTime1 = stopTime1;
    }

    public String getStopTime2() {
        return stopTime2;
    }

    public void setStopTime2(String stopTime2) {
        this.stopTime2 = stopTime2;
    }

    public Integer getDeliveryInterval() {
        return deliveryInterval;
    }

    public void setDeliveryInterval(Integer deliveryInterval) {
        this.deliveryInterval = deliveryInterval;
    }

    public BigDecimal getDeliveryBuySlippage() {
        return deliveryBuySlippage;
    }

    public void setDeliveryBuySlippage(BigDecimal deliveryBuySlippage) {
        this.deliveryBuySlippage = deliveryBuySlippage;
    }

    public BigDecimal getDeliverySellSlippage() {
        return deliverySellSlippage;
    }

    public void setDeliverySellSlippage(BigDecimal deliverySellSlippage) {
        this.deliverySellSlippage = deliverySellSlippage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}