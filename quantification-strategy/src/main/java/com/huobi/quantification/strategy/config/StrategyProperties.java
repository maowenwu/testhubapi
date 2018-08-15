package com.huobi.quantification.strategy.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "strategy")
public class StrategyProperties {



    private ConfigGroup group = new ConfigGroup();

    public ConfigGroup getGroup() {
        return group;
    }

    public void setGroup(ConfigGroup group) {
        this.group = group;
    }

    public static class ConfigGroup {
        private String name;
        private Long version = System.currentTimeMillis();
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
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }
    }

    public static class Config {
        private Integer exchangeId;
        private Long accountId;
        private String baseCoin;
        private String quotCoin;
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
