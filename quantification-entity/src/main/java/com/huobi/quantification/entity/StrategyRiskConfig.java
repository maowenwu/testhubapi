package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyRiskConfig {
    /**
     * @mbg.generated 2018-08-02 15:18:56
     */
    private Integer id;

    /**
     * 币对
     * @mbg.generated 2018-08-02 15:18:56
     */
    private String symbol;

    /**
     * 合约类型
     * @mbg.generated 2018-08-02 15:18:56
     */
    private String contractType;

    /**
     * 保证金率限制1
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal riskRateLevel1;

    /**
     * 保证金率限制2
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal riskRateLevel2;

    /**
     * 保证金率限制3
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal riskRateLevel3;

    /**
     * 净头寸阈值1
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal netPositionLevel1;

    /**
     * 净头寸阈值2
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal netPositionLevel2;

    /**
     * 单次盈亏阈值
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal currProfitLevel1;

    /**
     * 单次盈亏阈值2
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal currProfitLevel2;

    /**
     * 总盈亏阈值1
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal totalProfitLevel1;

    /**
     * 总盈亏阈值2
     * @mbg.generated 2018-08-02 15:18:56
     */
    private BigDecimal totalProfitLevel2;

    /**
     * @mbg.generated 2018-08-02 15:18:56
     */
    private Integer tradeCtrl;

    /**
     * @mbg.generated 2018-08-02 15:18:56
     */
    private Integer hedgeCtrl;

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

    public BigDecimal getRiskRateLevel1() {
        return riskRateLevel1;
    }

    public void setRiskRateLevel1(BigDecimal riskRateLevel1) {
        this.riskRateLevel1 = riskRateLevel1;
    }

    public BigDecimal getRiskRateLevel2() {
        return riskRateLevel2;
    }

    public void setRiskRateLevel2(BigDecimal riskRateLevel2) {
        this.riskRateLevel2 = riskRateLevel2;
    }

    public BigDecimal getRiskRateLevel3() {
        return riskRateLevel3;
    }

    public void setRiskRateLevel3(BigDecimal riskRateLevel3) {
        this.riskRateLevel3 = riskRateLevel3;
    }

    public BigDecimal getNetPositionLevel1() {
        return netPositionLevel1;
    }

    public void setNetPositionLevel1(BigDecimal netPositionLevel1) {
        this.netPositionLevel1 = netPositionLevel1;
    }

    public BigDecimal getNetPositionLevel2() {
        return netPositionLevel2;
    }

    public void setNetPositionLevel2(BigDecimal netPositionLevel2) {
        this.netPositionLevel2 = netPositionLevel2;
    }

    public BigDecimal getCurrProfitLevel1() {
        return currProfitLevel1;
    }

    public void setCurrProfitLevel1(BigDecimal currProfitLevel1) {
        this.currProfitLevel1 = currProfitLevel1;
    }

    public BigDecimal getCurrProfitLevel2() {
        return currProfitLevel2;
    }

    public void setCurrProfitLevel2(BigDecimal currProfitLevel2) {
        this.currProfitLevel2 = currProfitLevel2;
    }

    public BigDecimal getTotalProfitLevel1() {
        return totalProfitLevel1;
    }

    public void setTotalProfitLevel1(BigDecimal totalProfitLevel1) {
        this.totalProfitLevel1 = totalProfitLevel1;
    }

    public BigDecimal getTotalProfitLevel2() {
        return totalProfitLevel2;
    }

    public void setTotalProfitLevel2(BigDecimal totalProfitLevel2) {
        this.totalProfitLevel2 = totalProfitLevel2;
    }

    public Integer getTradeCtrl() {
        return tradeCtrl;
    }

    public void setTradeCtrl(Integer tradeCtrl) {
        this.tradeCtrl = tradeCtrl;
    }

    public Integer getHedgeCtrl() {
        return hedgeCtrl;
    }

    public void setHedgeCtrl(Integer hedgeCtrl) {
        this.hedgeCtrl = hedgeCtrl;
    }
}