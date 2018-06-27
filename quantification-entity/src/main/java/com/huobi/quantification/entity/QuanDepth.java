package com.huobi.quantification.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class QuanDepth implements Serializable {
    private Long id;

    private String depthStatus;

    private Long depthExchangeId;

    private Long depthTs;

    private String depthCh;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepthStatus() {
        return depthStatus;
    }

    public void setDepthStatus(String depthStatus) {
        this.depthStatus = depthStatus;
    }

    public Long getDepthExchangeId() {
        return depthExchangeId;
    }

    public void setDepthExchangeId(Long depthExchangeId) {
        this.depthExchangeId = depthExchangeId;
    }

    public Long getDepthTs() {
        return depthTs;
    }

    public void setDepthTs(Long depthTs) {
        this.depthTs = depthTs;
    }

    public String getDepthCh() {
        return depthCh;
    }

    public void setDepthCh(String depthCh) {
        this.depthCh = depthCh;
    }
}