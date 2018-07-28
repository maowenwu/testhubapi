package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyRiskConfig {
    /**
     * @mbg.generated 2018-07-26 17:39:17
     */
    private Integer id;

    /**
     * 保存比例
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal saveRate;

    /**
     * 总持仓量
     * @mbg.generated 2018-07-26 17:39:17
     */
    private Integer totalInventoryAmount;

    /**
     * 保证金率限制1
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal marginRateA;

    /**
     * 保证金率限制2
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal marginRateB;

    /**
     * 保证金率限制3
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal marginRateC;

    /**
     * 净头寸阈值1
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal positionA;

    /**
     * 净头寸阈值2
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal positionB;

    /**
     * 单次盈亏阈值
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal onceProfitLossA;

    /**
     * 单次盈亏阈值2
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal onceProfitLossB;

    /**
     * 总盈亏阈值1
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal totalProfitLossA;

    /**
     * 总盈亏阈值2
     * @mbg.generated 2018-07-26 17:39:17
     */
    private BigDecimal totalProfitLossB;

    /**
     * 币对
     * @mbg.generated 2018-07-26 17:39:17
     */
    private String symbol;

    /**
     * 合约代码
     * @mbg.generated 2018-07-26 17:39:17
     */
    private String contractType;

    /**
     * @mbg.generated 2018-07-26 17:39:17
     */
    private String contractCode;

    /**
     * 对摆单模块的设置
     * @mbg.generated 2018-07-26 17:39:17
     */
    private Integer orderRiskManagement;

    /**
     * 对冲模块风控处理，0正常运行，1停止
     * @mbg.generated 2018-07-26 17:39:17
     */
    private Integer hedgingRiskManagement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getSaveRate() {
        return saveRate;
    }

    public void setSaveRate(BigDecimal saveRate) {
        this.saveRate = saveRate;
    }

    public Integer getTotalInventoryAmount() {
        return totalInventoryAmount;
    }

    public void setTotalInventoryAmount(Integer totalInventoryAmount) {
        this.totalInventoryAmount = totalInventoryAmount;
    }

    public BigDecimal getMarginRateA() {
        return marginRateA;
    }

    public void setMarginRateA(BigDecimal marginRateA) {
        this.marginRateA = marginRateA;
    }

    public BigDecimal getMarginRateB() {
        return marginRateB;
    }

    public void setMarginRateB(BigDecimal marginRateB) {
        this.marginRateB = marginRateB;
    }

    public BigDecimal getMarginRateC() {
        return marginRateC;
    }

    public void setMarginRateC(BigDecimal marginRateC) {
        this.marginRateC = marginRateC;
    }

    public BigDecimal getPositionA() {
        return positionA;
    }

    public void setPositionA(BigDecimal positionA) {
        this.positionA = positionA;
    }

    public BigDecimal getPositionB() {
        return positionB;
    }

    public void setPositionB(BigDecimal positionB) {
        this.positionB = positionB;
    }

    public BigDecimal getOnceProfitLossA() {
        return onceProfitLossA;
    }

    public void setOnceProfitLossA(BigDecimal onceProfitLossA) {
        this.onceProfitLossA = onceProfitLossA;
    }

    public BigDecimal getOnceProfitLossB() {
        return onceProfitLossB;
    }

    public void setOnceProfitLossB(BigDecimal onceProfitLossB) {
        this.onceProfitLossB = onceProfitLossB;
    }

    public BigDecimal getTotalProfitLossA() {
        return totalProfitLossA;
    }

    public void setTotalProfitLossA(BigDecimal totalProfitLossA) {
        this.totalProfitLossA = totalProfitLossA;
    }

    public BigDecimal getTotalProfitLossB() {
        return totalProfitLossB;
    }

    public void setTotalProfitLossB(BigDecimal totalProfitLossB) {
        this.totalProfitLossB = totalProfitLossB;
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getOrderRiskManagement() {
        return orderRiskManagement;
    }

    public void setOrderRiskManagement(Integer orderRiskManagement) {
        this.orderRiskManagement = orderRiskManagement;
    }

    public Integer getHedgingRiskManagement() {
        return hedgingRiskManagement;
    }

    public void setHedgingRiskManagement(Integer hedgingRiskManagement) {
        this.hedgingRiskManagement = hedgingRiskManagement;
    }
}