package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class SpotBalanceRespDto implements Serializable {

    private static final long serialVersionUID = -1659538573294922052L;

    private Map<String, DataBean> data;

    public Map<String, DataBean> getData() {
        return data;
    }

    public void setData(Map<String, DataBean> data) {
        this.data = data;
    }


    public static class DataBean implements Serializable {

        private static final long serialVersionUID = -8504403035745178930L;
        private BigDecimal total;//总结
        private BigDecimal available;//可用
        private BigDecimal frozen;//冻结

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getAvailable() {
            return available;
        }

        public void setAvailable(BigDecimal available) {
            this.available = available;
        }

        public BigDecimal getFrozen() {
            return frozen;
        }

        public void setFrozen(BigDecimal frozen) {
            this.frozen = frozen;
        }
    }
}
