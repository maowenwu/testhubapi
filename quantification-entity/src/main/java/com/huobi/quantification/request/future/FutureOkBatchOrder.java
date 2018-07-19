package com.huobi.quantification.request.future;

import java.io.Serializable;

public class FutureOkBatchOrder implements Serializable {


    private String price;
    private String amount;
    private String type;
    private int matchPrice;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(int matchPrice) {
        this.matchPrice = matchPrice;
    }
}
