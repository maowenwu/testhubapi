package com.huobi.contract.index.entity;

public class HisPriceTimePointQueryBean {
    private String beginTime;
    private String endTime;
    private Integer interval;
    private Integer pointMount;
    private String symbol;
    private Long exchangeId;
    public HisPriceTimePointQueryBean() {
    }
    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getPointMount() {
        return pointMount;
    }

    public void setPointMount(Integer pointMount) {
        this.pointMount = pointMount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }


}
