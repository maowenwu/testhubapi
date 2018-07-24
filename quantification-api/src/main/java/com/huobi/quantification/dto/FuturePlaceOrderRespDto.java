package com.huobi.quantification.dto;

import java.io.Serializable;

public class FuturePlaceOrderRespDto implements Serializable {

    private Long innerOrderId;
    private Long exOrderId;
    private Long linkOrderId;

    public Long getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(Long innerOrderId) {
        this.innerOrderId = innerOrderId;
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
}
