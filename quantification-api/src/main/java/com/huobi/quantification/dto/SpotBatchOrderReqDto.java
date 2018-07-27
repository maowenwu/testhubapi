package com.huobi.quantification.dto;

import java.io.Serializable;
import java.util.List;

public class SpotBatchOrderReqDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6891851396200046298L;
	private int exchangeId;//交易所id
    private long accountId;//账户id
    private List<SpotBatchOrder> orders;//下单信息列表
    private boolean parallel = true;//是否并行下单
    private int timeInterval = 100;//休眠时间
    private boolean sync = true;//是否同步

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

    public List<SpotBatchOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<SpotBatchOrder> orders) {
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
