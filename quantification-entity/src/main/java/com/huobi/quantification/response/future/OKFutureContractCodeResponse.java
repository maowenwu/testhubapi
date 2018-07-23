package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

public class OKFutureContractCodeResponse {


    private int amount;
    @JSONField(name = "contract_name")
    private String contractCode;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
