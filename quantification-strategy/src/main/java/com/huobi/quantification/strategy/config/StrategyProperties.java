package com.huobi.quantification.strategy.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "strategy")
public class StrategyProperties {

    private ConfigGroup group1 = new ConfigGroup();
    private ConfigGroup group2 = new ConfigGroup();
    private ConfigGroup group3 = new ConfigGroup();

    public ConfigGroup getGroup1() {
        return group1;
    }

    public void setGroup1(ConfigGroup group1) {
        this.group1 = group1;
    }

    public ConfigGroup getGroup2() {
        return group2;
    }

    public void setGroup2(ConfigGroup group2) {
        this.group2 = group2;
    }

    public ConfigGroup getGroup3() {
        return group3;
    }

    public void setGroup3(ConfigGroup group3) {
        this.group3 = group3;
    }

    public static class ConfigGroup {
        private Boolean enable;
        private Config future = new Config();
        private Config spot = new Config();

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public Config getFuture() {
            return future;
        }

        public void setFuture(Config future) {
            this.future = future;
        }

        public Config getSpot() {
            return spot;
        }

        public void setSpot(Config spot) {
            this.spot = spot;
        }
    }

    public static class Config {
        private Integer exchangeId;
        private Long accountId;
        private String baseCoin;
        private String quotCoin;
        private String contractType;
        private String contractCode;
        private Integer lever;

        public Integer getExchangeId() {
            return exchangeId;
        }

        public void setExchangeId(Integer exchangeId) {
            this.exchangeId = exchangeId;
        }

        public Long getAccountId() {
            return accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
        }

        public String getBaseCoin() {
            return baseCoin;
        }

        public void setBaseCoin(String baseCoin) {
            this.baseCoin = baseCoin;
        }

        public String getQuotCoin() {
            return quotCoin;
        }

        public void setQuotCoin(String quotCoin) {
            this.quotCoin = quotCoin;
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

        public Integer getLever() {
            return lever;
        }

        public void setLever(Integer lever) {
            this.lever = lever;
        }
    }

}
