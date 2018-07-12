package com.huobi.quantification.response.future;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

public class OKFutureIndexResponse {


    @JSONField(name = "future_index")
    private BigDecimal futureIndex;

    public BigDecimal getFutureIndex() {
        return futureIndex;
    }

    public void setFutureIndex(BigDecimal futureIndex) {
        this.futureIndex = futureIndex;
    }
}
