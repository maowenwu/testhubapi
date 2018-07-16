package com.huobi.quantification.dto;

public class FutureOrderRespDto {

    private long innerOrderId;
    private long exOrderId;
    private long linkOrderId;

    public long getInnerOrderId() {
        return innerOrderId;
    }

    public void setInnerOrderId(long innerOrderId) {
        this.innerOrderId = innerOrderId;
    }

    public long getExOrderId() {
        return exOrderId;
    }

    public void setExOrderId(long exOrderId) {
        this.exOrderId = exOrderId;
    }

    public long getLinkOrderId() {
        return linkOrderId;
    }

    public void setLinkOrderId(long linkOrderId) {
        this.linkOrderId = linkOrderId;
    }
}
