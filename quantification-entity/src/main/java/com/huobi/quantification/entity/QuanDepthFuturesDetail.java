package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanDepthFuturesDetail implements Serializable {
    private Long id;

    /**
     * 深度ID
     */
    private Long depthId;

    /**
     * 价格
     */
    private Long detailPrice;

    /**
     * 数量
     */
    private Double detailAmount;

    /**
     * 更新时间
     */
    private Date dateUpdate;

    private static final long serialVersionUID = 1L;

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

    public Long getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(Long detailPrice) {
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