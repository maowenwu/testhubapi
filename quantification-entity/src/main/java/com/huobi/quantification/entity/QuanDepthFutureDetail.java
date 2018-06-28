package com.huobi.quantification.entity;

import java.math.BigDecimal;
import java.util.Date;

public class QuanDepthFutureDetail {
    /**
     * @mbg.generated 2018-06-28 15:27:19
     */
    private Long id;

    /**
     * 深度ID
     * @mbg.generated 2018-06-28 15:27:19
     */
    private Long depthFutureId;

    /**
     * @mbg.generated 2018-06-28 15:27:19
     */
    private Integer detailType;

    /**
     * 价格
     * @mbg.generated 2018-06-28 15:27:19
     */
    private BigDecimal detailPrice;

    /**
     * 数量
     * @mbg.generated 2018-06-28 15:27:19
     */
    private Double detailAmount;

    /**
     * 更新时间
     * @mbg.generated 2018-06-28 15:27:19
     */
    private Date dateUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepthFutureId() {
        return depthFutureId;
    }

    public void setDepthFutureId(Long depthFutureId) {
        this.depthFutureId = depthFutureId;
    }

    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
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