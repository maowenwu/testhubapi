package com.huobi.quantification.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FuturePriceOrderRespDto implements Serializable {

    private Map<BigDecimal, List<DataBean>> priceOrderMap;

    public Map<BigDecimal, List<DataBean>> getPriceOrderMap() {
        return priceOrderMap;
    }

    public void setPriceOrderMap(Map<BigDecimal, List<DataBean>> priceOrderMap) {
        this.priceOrderMap = priceOrderMap;
    }

    public static class DataBean implements Serializable{
        private Long innerOrderId;
        private Long exOrderId;
        private Long linkOrderId;
        private String baseCoin;
        private String quoteCoin;
        private String contractType;
        private String contractCode;
        private Integer status;
        private Integer sourceStatus;
        private Integer side;
        private Integer offset;
        private Integer lever;
        private String orderType;
        private BigDecimal orderPrice;
        private BigDecimal dealPrice;
        private BigDecimal orderQty;
        private BigDecimal dealQty;
        private BigDecimal remainingQty;
        private BigDecimal marginFrozen;
        private BigDecimal fees;
        private Date createDate;
        private Date updateDate;

        public Long getInnerOrderId() {
            return innerOrderId;
        }

        public void setInnerOrderId(Long innerOrderId) {
            this.innerOrderId = innerOrderId;
        }

        public Long getExOrderId() {
            return exOrderId;
        }

        public void setExOrderId(Long exOrderId) {
            this.exOrderId = exOrderId;
        }

        public Long getLinkOrderId() {
            return linkOrderId;
        }

        public void setLinkOrderId(Long linkOrderId) {
            this.linkOrderId = linkOrderId;
        }

        public String getBaseCoin() {
            return baseCoin;
        }

        public void setBaseCoin(String baseCoin) {
            this.baseCoin = baseCoin;
        }

        public String getQuoteCoin() {
            return quoteCoin;
        }

        public void setQuoteCoin(String quoteCoin) {
            this.quoteCoin = quoteCoin;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getSourceStatus() {
            return sourceStatus;
        }

        public void setSourceStatus(Integer sourceStatus) {
            this.sourceStatus = sourceStatus;
        }

        public Integer getSide() {
            return side;
        }

        public void setSide(Integer side) {
            this.side = side;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getLever() {
            return lever;
        }

        public void setLever(Integer lever) {
            this.lever = lever;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public BigDecimal getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(BigDecimal orderPrice) {
            this.orderPrice = orderPrice;
        }

        public BigDecimal getDealPrice() {
            return dealPrice;
        }

        public void setDealPrice(BigDecimal dealPrice) {
            this.dealPrice = dealPrice;
        }

        public BigDecimal getOrderQty() {
            return orderQty;
        }

        public void setOrderQty(BigDecimal orderQty) {
            this.orderQty = orderQty;
        }

        public BigDecimal getDealQty() {
            return dealQty;
        }

        public void setDealQty(BigDecimal dealQty) {
            this.dealQty = dealQty;
        }

        public BigDecimal getRemainingQty() {
            return remainingQty;
        }

        public void setRemainingQty(BigDecimal remainingQty) {
            this.remainingQty = remainingQty;
        }

        public BigDecimal getMarginFrozen() {
            return marginFrozen;
        }

        public void setMarginFrozen(BigDecimal marginFrozen) {
            this.marginFrozen = marginFrozen;
        }

        public BigDecimal getFees() {
            return fees;
        }

        public void setFees(BigDecimal fees) {
            this.fees = fees;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public Date getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
        }
    }
}
