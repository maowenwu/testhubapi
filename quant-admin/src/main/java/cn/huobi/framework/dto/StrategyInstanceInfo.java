package cn.huobi.framework.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StrategyInstanceInfo implements Serializable {

    private Integer id;
    private String strategyName;
    private Long instanceId;
    private Integer instanceEnable;
    private String instanceGroup;
    private Integer futureExchangeId;
    private Long futureAccountId;
    private String futureBaseCoin;
    private String futureQuotCoin;
    private String futureContractCode;
    private Integer futureLever;
    private Integer spotExchangeId;
    private Long spotAccountId;
    private String spotBaseCoin;
    private String spotQuotCoin;
    private String spotDepthType;
    private String futureDepthType;
    private BigDecimal totalProfit;//总盈亏

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getInstanceEnable() {
        return instanceEnable;
    }

    public void setInstanceEnable(Integer instanceEnable) {
        this.instanceEnable = instanceEnable;
    }

    public String getInstanceGroup() {
        return instanceGroup;
    }

    public void setInstanceGroup(String instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

    public Integer getFutureExchangeId() {
        return futureExchangeId;
    }

    public void setFutureExchangeId(Integer futureExchangeId) {
        this.futureExchangeId = futureExchangeId;
    }

    public Long getFutureAccountId() {
        return futureAccountId;
    }

    public void setFutureAccountId(Long futureAccountId) {
        this.futureAccountId = futureAccountId;
    }

    public String getFutureBaseCoin() {
        return futureBaseCoin;
    }

    public void setFutureBaseCoin(String futureBaseCoin) {
        this.futureBaseCoin = futureBaseCoin;
    }

    public String getFutureQuotCoin() {
        return futureQuotCoin;
    }

    public void setFutureQuotCoin(String futureQuotCoin) {
        this.futureQuotCoin = futureQuotCoin;
    }

    public String getFutureContractCode() {
        return futureContractCode;
    }

    public void setFutureContractCode(String futureContractCode) {
        this.futureContractCode = futureContractCode;
    }

    public Integer getFutureLever() {
        return futureLever;
    }

    public void setFutureLever(Integer futureLever) {
        this.futureLever = futureLever;
    }

    public Integer getSpotExchangeId() {
        return spotExchangeId;
    }

    public void setSpotExchangeId(Integer spotExchangeId) {
        this.spotExchangeId = spotExchangeId;
    }

    public Long getSpotAccountId() {
        return spotAccountId;
    }

    public void setSpotAccountId(Long spotAccountId) {
        this.spotAccountId = spotAccountId;
    }

    public String getSpotBaseCoin() {
        return spotBaseCoin;
    }

    public void setSpotBaseCoin(String spotBaseCoin) {
        this.spotBaseCoin = spotBaseCoin;
    }

    public String getSpotQuotCoin() {
        return spotQuotCoin;
    }

    public void setSpotQuotCoin(String spotQuotCoin) {
        this.spotQuotCoin = spotQuotCoin;
    }

    public String getSpotDepthType() {
        return spotDepthType;
    }

    public void setSpotDepthType(String spotDepthType) {
        this.spotDepthType = spotDepthType;
    }

    public String getFutureDepthType() {
        return futureDepthType;
    }

    public void setFutureDepthType(String futureDepthType) {
        this.futureDepthType = futureDepthType;
    }


}