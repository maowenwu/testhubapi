package com.huobi.contract.index.dto;

public class ContractPriceIndexHisCount {
    private Long exchangeId;
    private String indexSymbol;
    private Long count;

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getIndexSymbol() {
        return indexSymbol;
    }

    public void setIndexSymbol(String indexSymbol) {
        this.indexSymbol = indexSymbol;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
