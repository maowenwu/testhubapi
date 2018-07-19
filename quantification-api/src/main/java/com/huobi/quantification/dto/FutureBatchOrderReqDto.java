package com.huobi.quantification.dto;

import java.io.Serializable;
import java.util.List;

public class FutureBatchOrderReqDto implements Serializable {
    private int exchangeId;
    private long accountId;
    private List<FutureBatchOrder> orders;
    private boolean parallel;
    private int timeInterval;
    private boolean sync;

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public List<FutureBatchOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<FutureBatchOrder> orders) {
        this.orders = orders;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
