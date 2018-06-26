package com.huobi.contract.index.contract.index.service;

import com.huobi.contract.index.entity.IpPool;

public interface IpPoolQueue {
    /**
     * 维护IP池队列
     */
    void holdIpPoolQueue();

    IpPool getFristIpPool(String exchangeName);

    void setIpPoolToQueueLast(String exchangeName,IpPool ipPool);

    long getIpQueueLength(String exchangeName);
}
