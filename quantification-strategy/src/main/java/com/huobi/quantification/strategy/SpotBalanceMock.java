package com.huobi.quantification.strategy;

import com.huobi.quantification.strategy.entity.SpotBalance;

import java.math.BigDecimal;

public class SpotBalanceMock {

    private static SpotBalance spotBalance = new SpotBalance();

    static {
        SpotBalance.Coin coin = new SpotBalance.Coin();
        coin.setAvailable(BigDecimal.valueOf(10000 * 100));
        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        usdt.setAvailable(BigDecimal.valueOf(10000 * 100));
        spotBalance.setCoin(coin);
        spotBalance.setUsdt(usdt);
    }

    public static SpotBalance getSpotBalance() {
        return spotBalance;
    }

    public static void setCoin(BigDecimal coin) {
        spotBalance.getCoin().setAvailable(coin);
    }

    public static BigDecimal getCoin() {
        return spotBalance.getCoin().getAvailable();
    }

    public static void setUsdt(BigDecimal usdt) {
        spotBalance.getUsdt().setAvailable(usdt);
    }

    public static BigDecimal getUsdt() {
        return spotBalance.getUsdt().getAvailable();
    }
}
