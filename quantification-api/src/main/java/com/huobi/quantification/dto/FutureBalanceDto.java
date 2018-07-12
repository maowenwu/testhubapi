package com.huobi.quantification.dto;

public class FutureBalanceDto {


    private int ts;
    private DataBean data;

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private BtcBean btc;

        public BtcBean getBtc() {
            return btc;
        }

        public void setBtc(BtcBean btc) {
            this.btc = btc;
        }

        public static class BtcBean {
            private int margin_balance;
            private int margin_position;
            private int margin_frozen;
            private int margin_available;
            private int profit_real;
            private int profit_unreal;
            private int risk_rate;

            public int getMargin_balance() {
                return margin_balance;
            }

            public void setMargin_balance(int margin_balance) {
                this.margin_balance = margin_balance;
            }

            public int getMargin_position() {
                return margin_position;
            }

            public void setMargin_position(int margin_position) {
                this.margin_position = margin_position;
            }

            public int getMargin_frozen() {
                return margin_frozen;
            }

            public void setMargin_frozen(int margin_frozen) {
                this.margin_frozen = margin_frozen;
            }

            public int getMargin_available() {
                return margin_available;
            }

            public void setMargin_available(int margin_available) {
                this.margin_available = margin_available;
            }

            public int getProfit_real() {
                return profit_real;
            }

            public void setProfit_real(int profit_real) {
                this.profit_real = profit_real;
            }

            public int getProfit_unreal() {
                return profit_unreal;
            }

            public void setProfit_unreal(int profit_unreal) {
                this.profit_unreal = profit_unreal;
            }

            public int getRisk_rate() {
                return risk_rate;
            }

            public void setRisk_rate(int risk_rate) {
                this.risk_rate = risk_rate;
            }
        }
    }
}
