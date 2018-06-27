package com.huobi.contract.index.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @desc
 * @Author mingjianyong
 */
public class SymbolPriceDiffScaleAvg implements Serializable {

    private static final long serialVersionUID = 1241189918683475660L;
    private String symbol;
    private BigDecimal scale;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }
}
