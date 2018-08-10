package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanOrderFuture {
    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Long innerOrderId;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String strategyName;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Long strategyVersion;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer exchangeId;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Long accountId;

    /**
     * 交易所id
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Long exOrderId;

    /**
     * 账户 ID
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Long linkOrderId;

    /**
     * 火币或ok返回的订单id
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String baseCoin;

    /**
     * 合约名称
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String quoteCoin;

    /**
     * btc_usd   ltc_usd    eth_usd    etc_usd    bch_usd
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String contractType;

    /**
     * 订单类型 1：开多 2：开空 3：平多 4： 平空
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String contractCode;

    /**
     * 订单状态(0等待成交 1部分成交 2全部成交 -1撤单 4撤单处理中 5撤单中)
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer status;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer sourceStatus;

    /**
     * 委托数量
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer side;

    /**
     * 成交数量
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer offset;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Integer lever;

    /**
     * 手续费
     * @mbg.generated 2018-07-24 14:21:12
     */
    private String orderType;

    /**
     * 订单价格
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal orderPrice;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal dealPrice;

    /**
     * 平均价格
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal orderQty;

    /**
     * 杠杆倍数  value:10\20  默认10 
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal dealQty;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal remainingQty;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal marginFrozen;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private BigDecimal fees;

    /**
     * 委托时间
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Date createDate;

    /**
     * @mbg.generated 2018-07-24 14:21:12
     */
    private Date updateDate;

    private String orderSource;//订单来源

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Long getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(Long innerOrderId) {
        this.innerOrderId = innerOrderId;
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

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getExOrderId() {
        return exOrderId;
    }

    public void setExOrderId(Long exOrderId) {
        this.exOrderId = exOrderId;
    }

    public Long getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(Long linkOrderId) {
        this.linkOrderId = linkOrderId;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getQuoteCoin() {
        return quoteCoin;
    }

    public void setQuoteCoin(String quoteCoin) {
        this.quoteCoin = quoteCoin;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(Integer sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLever() {
        return lever;
    }

    public void setLever(Integer lever) {
        this.lever = lever;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getDealQty() {
        return dealQty;
    }

    public void setDealQty(BigDecimal dealQty) {
        this.dealQty = dealQty;
    }

    public BigDecimal getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(BigDecimal remainingQty) {
        this.remainingQty = remainingQty;
    }

    public BigDecimal getMarginFrozen() {
        return marginFrozen;
    }

    public void setMarginFrozen(BigDecimal marginFrozen) {
        this.marginFrozen = marginFrozen;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}