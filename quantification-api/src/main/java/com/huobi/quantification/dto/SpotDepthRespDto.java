package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author lichenyang
 * @since  2018年7月13日
 */
public class SpotDepthRespDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2135267412924776786L;
	private Date ts;
    private DataBean data;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 6386914347198020191L;
		private List<Depth> asks;
        private List<Depth> bids;

        public List<Depth> getAsks() {
            return asks;
        }

        public void setAsks(List<Depth> asks) {
            this.asks = asks;
        }

        public List<Depth> getBids() {
            return bids;
        }

        public void setBids(List<Depth> bids) {
            this.bids = bids;
        }
    }

    public static class Depth implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 3011362207412093277L;
		private BigDecimal price;
        private BigDecimal amount;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
