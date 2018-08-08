package com.huobi.quantification.entity;

import java.math.BigDecimal;

public class StrategyRiskConfig {
    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer id;

    /**
     * 币对
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String symbol;

    /**
     * 合约类型
     * @mbg.generated 2018-08-08 16:28:34
     */
    private String contractType;

    /**
     * 保证金率限制1
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal riskRateLevel1;

    /**
     * 保证金率限制2
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal riskRateLevel2;

    /**
     * 保证金率限制3
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal riskRateLevel3;

    /**
     * 强平买单滑点
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal buyCloseSlippage;

    /**
     * 强平卖单滑点
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal sellCloseSlippage;

    /**
     * 强平单单笔最大数量（张）
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal closeAmount;

    /**
     * 净头寸阈值1
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal netPositionLevel1;

    /**
     * 净头寸阈值2
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal netPositionLevel2;

    /**
     * 单次盈亏阈值
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal currProfitLevel1;

    /**
     * 单次盈亏阈值2
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal currProfitLevel2;

    /**
     * 总盈亏阈值1
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal totalProfitLevel1;

    /**
     * 总盈亏阈值2
     * @mbg.generated 2018-08-08 16:28:34
     */
    private BigDecimal totalProfitLevel2;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer riskOrderCtrl;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer riskHedgeCtrl;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer netOrderCtrl;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer netHedgeCtrl;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer profitOrderCtrl;

    /**
     * @mbg.generated 2018-08-08 16:28:34
     */
    private Integer profitHedgeCtrl;

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

    public BigDecimal getBuyCloseSlippage() {
        return buyCloseSlippage;
    }

    public void setBuyCloseSlippage(BigDecimal buyCloseSlippage) {
        this.buyCloseSlippage = buyCloseSlippage;
    }

    public BigDecimal getSellCloseSlippage() {
        return sellCloseSlippage;
    }

    public void setSellCloseSlippage(BigDecimal sellCloseSlippage) {
        this.sellCloseSlippage = sellCloseSlippage;
    }

    public BigDecimal getCloseAmount() {
        return closeAmount;
    }

    public void setCloseAmount(BigDecimal closeAmount) {
        this.closeAmount = closeAmount;
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

    public Integer getRiskOrderCtrl() {
        return riskOrderCtrl;
    }

    public void setRiskOrderCtrl(Integer riskOrderCtrl) {
        this.riskOrderCtrl = riskOrderCtrl;
    }

    public Integer getRiskHedgeCtrl() {
        return riskHedgeCtrl;
    }

    public void setRiskHedgeCtrl(Integer riskHedgeCtrl) {
        this.riskHedgeCtrl = riskHedgeCtrl;
    }

    public Integer getNetOrderCtrl() {
        return netOrderCtrl;
    }

    public void setNetOrderCtrl(Integer netOrderCtrl) {
        this.netOrderCtrl = netOrderCtrl;
    }

    public Integer getNetHedgeCtrl() {
        return netHedgeCtrl;
    }

    public void setNetHedgeCtrl(Integer netHedgeCtrl) {
        this.netHedgeCtrl = netHedgeCtrl;
    }

    public Integer getProfitOrderCtrl() {
        return profitOrderCtrl;
    }

    public void setProfitOrderCtrl(Integer profitOrderCtrl) {
        this.profitOrderCtrl = profitOrderCtrl;
    }

    public Integer getProfitHedgeCtrl() {
        return profitHedgeCtrl;
    }

    public void setProfitHedgeCtrl(Integer profitHedgeCtrl) {
        this.profitHedgeCtrl = profitHedgeCtrl;
    }
}