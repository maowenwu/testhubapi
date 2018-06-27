package com.huobi.quantification.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class QuanDepthDetail implements Serializable {
    private Long id;

    private Long depthId;

    private String detailType;

    private Long detailPrice;

    private Double detailAmount;

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

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
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