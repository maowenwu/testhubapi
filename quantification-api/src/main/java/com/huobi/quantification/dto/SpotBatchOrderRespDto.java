package com.huobi.quantification.dto;

import java.io.Serializable;

public class SpotBatchOrderRespDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2270599807867202696L;
	private long innerOrderId;//内部订单id
    private long exOrderId;//交易所订单id
    private long linkOrderId;//关联订单id

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
