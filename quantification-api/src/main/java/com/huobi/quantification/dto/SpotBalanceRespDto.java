package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.huobi.quantification.dto.FutureBalanceRespDto.DataBean;

public class SpotBalanceRespDto implements Serializable{
	private Date ts;
    private Map<String,DataBean> data;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Map<String, DataBean> getData() {
        return data;
    }

    public void setData(Map<String, DataBean> data) {
        this.data = data;
    }
    
    public static class DataBean implements Serializable{
    	private BigDecimal total;
    	private BigDecimal available;
    	private BigDecimal frozen;
    	
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
