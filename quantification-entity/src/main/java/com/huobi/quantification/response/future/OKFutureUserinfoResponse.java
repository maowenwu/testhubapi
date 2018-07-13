package com.huobi.quantification.response.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

// {"result":true,"info":{"btc":{"risk_rate":10000,"account_rights":0.00304752,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"btg":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"etc":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"bch":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"xrp":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"eth":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"eos":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0},"ltc":{"risk_rate":10000,"account_rights":0,"profit_unreal":0,"profit_real":0,"keep_deposit":0}}}
public class OKFutureUserinfoResponse {


    private boolean result;
    private InfoBean info;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private DataBean btc;
        private DataBean btg;
        private DataBean etc;
        private DataBean bch;
        private DataBean xrp;
        private DataBean eth;
        private DataBean eos;
        private DataBean ltc;

        public DataBean getBtc() {
            return btc;
        }

        public void setBtc(DataBean btc) {
            this.btc = btc;
        }

        public DataBean getBtg() {
            return btg;
        }

        public void setBtg(DataBean btg) {
            this.btg = btg;
        }

        public DataBean getEtc() {
            return etc;
        }

        public void setEtc(DataBean etc) {
            this.etc = etc;
        }

        public DataBean getBch() {
            return bch;
        }

        public void setBch(DataBean bch) {
            this.bch = bch;
        }

        public DataBean getXrp() {
            return xrp;
        }

        public void setXrp(DataBean xrp) {
            this.xrp = xrp;
        }

        public DataBean getEth() {
            return eth;
        }

        public void setEth(DataBean eth) {
            this.eth = eth;
        }

        public DataBean getEos() {
            return eos;
        }

        public void setEos(DataBean eos) {
            this.eos = eos;
        }

        public DataBean getLtc() {
            return ltc;
        }

        public void setLtc(DataBean ltc) {
            this.ltc = ltc;
        }
    }


    public static class DataBean {
        @JSONField(name = "risk_rate")
        private BigDecimal riskRate;
        @JSONField(name = "account_rights")
        private BigDecimal accountRights;
        @JSONField(name = "profit_unreal")
        private BigDecimal profitUnreal;
        @JSONField(name = "profit_real")
        private BigDecimal profitReal;
        @JSONField(name = "keep_deposit")
        private BigDecimal keepDeposit;

        public BigDecimal getRiskRate() {
            return riskRate;
        }

        public void setRiskRate(BigDecimal riskRate) {
            this.riskRate = riskRate;
        }

        public BigDecimal getAccountRights() {
            return accountRights;
        }

        public void setAccountRights(BigDecimal accountRights) {
            this.accountRights = accountRights;
        }

        public BigDecimal getProfitUnreal() {
            return profitUnreal;
        }

        public void setProfitUnreal(BigDecimal profitUnreal) {
            this.profitUnreal = profitUnreal;
        }

        public BigDecimal getProfitReal() {
            return profitReal;
        }

        public void setProfitReal(BigDecimal profitReal) {
            this.profitReal = profitReal;
        }

        public BigDecimal getKeepDeposit() {
            return keepDeposit;
        }

        public void setKeepDeposit(BigDecimal keepDeposit) {
            this.keepDeposit = keepDeposit;
        }
    }

    public static void main(String[] args) {
        String s="{\"result\":true,\"info\":{\"btc\":{\"risk_rate\":10000,\"account_rights\":0.00304752,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"btg\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"etc\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"bch\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"xrp\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"eth\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"eos\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0},\"ltc\":{\"risk_rate\":10000,\"account_rights\":0,\"profit_unreal\":0,\"profit_real\":0,\"keep_deposit\":0}}}";
        OKFutureUserinfoResponse userinfoResponse = JSON.parseObject(s, OKFutureUserinfoResponse.class);
        System.out.println(userinfoResponse);
    }
}
