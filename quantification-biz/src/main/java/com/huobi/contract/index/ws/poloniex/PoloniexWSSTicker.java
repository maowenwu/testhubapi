package com.huobi.contract.index.ws.poloniex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;

/**
 * @author David
 */
public class PoloniexWSSTicker {

    public final Double currencyPair;
    public final BigDecimal lastPrice;
    public final BigDecimal lowestAsk;
    public final BigDecimal highestBid;
    public final BigDecimal percentChange;
    public final BigDecimal baseVolume;
    public final BigDecimal quoteVolume;
    public final Boolean isFrozen;
    public final BigDecimal twentyFourHourHigh;
    public final BigDecimal twentyFourHourLow;

    public PoloniexWSSTicker(Double currencyPair, BigDecimal lastPrice, BigDecimal lowestAsk, BigDecimal highestBid, BigDecimal percentChange, BigDecimal baseVolume, BigDecimal quoteVolume, Boolean isFrozen, BigDecimal twentyFourHourHigh, BigDecimal twentyFourHourLow) {
        this.currencyPair = currencyPair;
        this.lastPrice = lastPrice;
        this.lowestAsk = lowestAsk;
        this.highestBid = highestBid;
        this.percentChange = percentChange;
        this.baseVolume = baseVolume;
        this.quoteVolume = quoteVolume;
        this.isFrozen = isFrozen;
        this.twentyFourHourHigh = twentyFourHourHigh;
        this.twentyFourHourLow = twentyFourHourLow;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Double getCurrencyPair() {
        return currencyPair;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public BigDecimal getLowestAsk() {
        return lowestAsk;
    }

    public BigDecimal getHighestBid() {
        return highestBid;
    }

    public BigDecimal getPercentChange() {
        return percentChange;
    }

    public BigDecimal getBaseVolume() {
        return baseVolume;
    }

    public BigDecimal getQuoteVolume() {
        return quoteVolume;
    }

    public Boolean getFrozen() {
        return isFrozen;
    }

    public BigDecimal getTwentyFourHourHigh() {
        return twentyFourHourHigh;
    }

    public BigDecimal getTwentyFourHourLow() {
        return twentyFourHourLow;
    }


    public static PoloniexWSSTicker mapMessageToPoloniexTicker(String message) {
        JSONArray arr = JSON.parseArray(message);
        if (arr.size() < 3) {
            return null;
        }
        JSONArray jsonArray = arr.getJSONArray(2);
        return new PoloniexWSSTicker(
                jsonArray.getDouble(0),
                jsonArray.getBigDecimal(1),
                jsonArray.getBigDecimal(2),
                jsonArray.getBigDecimal(3),
                jsonArray.getBigDecimal(4),
                jsonArray.getBigDecimal(5),
                jsonArray.getBigDecimal(6),
                jsonArray.getBoolean(7),
                jsonArray.getBigDecimal(8),
                jsonArray.getBigDecimal(9));
    }
}
