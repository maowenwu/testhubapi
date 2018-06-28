package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanDepthDetail {
    /**
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long id;

    /**
     * 深度ID
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Long depthId;

    /**
     * 买卖盘类型
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Byte detailType;

    /**
     * 价格
     * @mbg.generated 2018-06-28 14:50:45
     */
    private BigDecimal detailPrice;

    /**
     * 数量
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Double detailAmount;

    /**
     * 更新时间
     * @mbg.generated 2018-06-28 14:50:45
     */
    private Date dateUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepthId() {
        return depthId;
    }

    public void setDepthId(Long depthId) {
        this.depthId = depthId;
    }

    public Byte getDetailType() {
        return detailType;
    }

    public void setDetailType(Byte detailType) {
        this.detailType = detailType;
    }

    public BigDecimal getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(BigDecimal detailPrice) {
        this.detailPrice = detailPrice;
    }

    public Double getDetailAmount() {
        return detailAmount;
    }

    public void setDetailAmount(Double detailAmount) {
        this.detailAmount = detailAmount;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}