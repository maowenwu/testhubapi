package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanOrderFuture {
    /**
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Long id;

    /**
     * @mbg.generated 2018-06-29 10:25:58
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Long strategyVersion;

    /**
     * 交易所id
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Long exchangeId;

    /**
     * 账户 ID
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Long orderAccountId;

    /**
     * 火币或ok返回的订单id
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Long orderSourceId;

    /**
     * 合约名称
     * @mbg.generated 2018-06-29 10:25:58
     */
    private String contractName;

    /**
     * btc_usd   ltc_usd    eth_usd    etc_usd    bch_usd
     * @mbg.generated 2018-06-29 10:25:58
     */
    private String orderSymbol;

    /**
     * 订单类型 1：开多 2：开空 3：平多 4： 平空
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Integer orderType;

    /**
     * 订单状态(0等待成交 1部分成交 2全部成交 -1撤单 4撤单处理中 5撤单中)
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Integer orderStatus;

    /**
     * 委托数量
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderAmount;

    /**
     * 成交数量
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderDealAmount;

    /**
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal unitAmount;

    /**
     * 手续费
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderFee;

    /**
     * 订单价格
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderPrice;

    /**
     * 平均价格
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderPriceAvg;

    /**
     * 杠杆倍数  value:10\20  默认10 
     * @mbg.generated 2018-06-29 10:25:58
     */
    private BigDecimal orderLeverRate;

    /**
     * 委托时间
     * @mbg.generated 2018-06-29 10:25:58
     */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Long getStrategyVersion() {
        return strategyVersion;
    }

    public void setStrategyVersion(Long strategyVersion) {
        this.strategyVersion = strategyVersion;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getOrderAccountId() {
        return orderAccountId;
    }

    public void setOrderAccountId(Long orderAccountId) {
        this.orderAccountId = orderAccountId;
    }

    public Long getOrderSourceId() {
        return orderSourceId;
    }

    public void setOrderSourceId(Long orderSourceId) {
        this.orderSourceId = orderSourceId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getOrderSymbol() {
        return orderSymbol;
    }

    public void setOrderSymbol(String orderSymbol) {
        this.orderSymbol = orderSymbol;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderDealAmount() {
        return orderDealAmount;
    }

    public void setOrderDealAmount(BigDecimal orderDealAmount) {
        this.orderDealAmount = orderDealAmount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPriceAvg() {
        return orderPriceAvg;
    }

    public void setOrderPriceAvg(BigDecimal orderPriceAvg) {
        this.orderPriceAvg = orderPriceAvg;
    }

    public BigDecimal getOrderLeverRate() {
        return orderLeverRate;
    }

    public void setOrderLeverRate(BigDecimal orderLeverRate) {
        this.orderLeverRate = orderLeverRate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}