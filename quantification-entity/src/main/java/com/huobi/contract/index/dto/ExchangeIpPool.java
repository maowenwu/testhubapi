package com.huobi.contract.index.dto;

import com.huobi.contract.index.entity.IpPool;

import java.io.Serializable;
import java.util.List;

/**
 * 交易所的IP队列
 */
public class ExchangeIpPool implements Serializable {

    private static final long serialVersionUID = 6305262771293041682L;

    private Long exchangeId;

    private Long grabIntervalTime;

    private Long ipUnfreezeTime;

    List<IpPool> ipPoolList;

    public ExchangeIpPool() {
    }

    public ExchangeIpPool(Long exchangeId, Long grabIntervalTime, Long ipUnfreezeTime, List<IpPool> ipPoolList) {
        this.exchangeId = exchangeId;
        this.grabIntervalTime = grabIntervalTime;
        this.ipUnfreezeTime = ipUnfreezeTime;
        this.ipPoolList = ipPoolList;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getGrabIntervalTime() {
        return grabIntervalTime;
    }

    public void setGrabIntervalTime(Long grabIntervalTime) {
        this.grabIntervalTime = grabIntervalTime;
    }

    public Long getIpUnfreezeTime() {
        return ipUnfreezeTime;
    }

    public void setIpUnfreezeTime(Long ipUnfreezeTime) {
        this.ipUnfreezeTime = ipUnfreezeTime;
    }

    public List<IpPool> getIpPoolList() {
        return ipPoolList;
    }

    public void setIpPoolList(List<IpPool> ipPoolList) {
        this.ipPoolList = ipPoolList;
    }
}
