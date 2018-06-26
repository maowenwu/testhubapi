package com.huobi.contract.index.dto;

import com.huobi.contract.index.entity.ExchangeRateHis;

import java.util.List;

/**
 * @desc
 * @Author mingjianyong
 */
public class ExchangeRateHisSymbol {
    private String symbol;
    private List<ExchangeRateHis> list;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<ExchangeRateHis> getList() {
        return list;
    }

    public void setList(List<ExchangeRateHis> list) {
        this.list = list;
    }
}
