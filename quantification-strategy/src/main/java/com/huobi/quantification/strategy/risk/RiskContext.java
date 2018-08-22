package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dao.StrategyRiskHistoryMapper;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.strategy.CommContext;
import com.huobi.quantification.strategy.entity.FutureBalance;
import com.huobi.quantification.strategy.entity.SpotBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class RiskContext {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FutureAccountService futureAccountService;

    @Autowired
    private SpotAccountService spotAccountService;
    @Autowired
    private QuanAccountAssetMapper quanAccountAssetMapper;
    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;
    @Autowired
    private StrategyRiskConfigMapper strategyRiskMapper;
    @Autowired
    private CommContext commContext;

    private String strategyName;
    private Long instanceId;
    private Integer instanceConfigId;

    private Integer futureExchangeId;
    private Long futureAccountId;
    private String futureBaseCoin;

    private Long spotAccountId;
    private Integer spotExchangeId;
    private String spotBaseCoin;
    private String spotQuoteCoin;


    /********本次期初余额*********/
    private SpotCoin currSpotCoin;
    private SpotUsdt currSpotUsdt;
    private FutureRight currFutureRight;
    /********历史期初余额*********/
    private SpotCoin initialSpotCoin;
    private SpotUsdt initialSpotUsdt;
    private FutureRight initialFutureRight;

    private BigDecimal currPrice;

    public void init(StrategyInstanceConfig config) {
        this.strategyName = config.getStrategyName();
        this.instanceId = config.getInstanceId();
        this.instanceConfigId = config.getId();

        this.futureExchangeId = config.getFutureExchangeId();
        this.futureAccountId = config.getFutureAccountId();
        this.futureBaseCoin = config.getFutureBaseCoin();

        this.spotExchangeId = config.getSpotExchangeId();
        this.spotAccountId = config.getSpotAccountId();
        this.spotBaseCoin = config.getSpotBaseCoin();
        this.spotQuoteCoin = config.getSpotQuotCoin();

        loadInitialProfit();
        loadCurrProfit();
    }


    /**
     * 加载账户最初数据
     * 币币账户期初余额BTC-币币账户期初净借贷BTC
     * 币币账户期初余额USDT-币币账户期初净借贷USDT
     * 合约账户期初权益BTC-合约账户期初净借贷BTC
     */
    private void loadInitialProfit() {
        // 加载币币账户期初余额
        QuanAccountAsset coinAccountAsset = quanAccountAssetMapper.selectByAccountSourceIdCoinType(spotAccountId, spotBaseCoin);
        if (coinAccountAsset == null) {
            throw new RuntimeException("[quan_account_asset]账户资产未初始化，spotAccountId=" + spotAccountId + "，spotBaseCoin={}" + spotBaseCoin);
        }
        SpotBalance.Coin coin = new SpotBalance.Coin();
        BeanUtils.copyProperties(coinAccountAsset, coin);
        BigDecimal coinNetBorrow = commContext.getNetBorrow(spotExchangeId, spotAccountId, spotBaseCoin, true);
        initialSpotCoin = new SpotCoin(coin, coinNetBorrow);
        // 加载币币账户usdt期初余额
        QuanAccountAsset usdtAccountAsset = quanAccountAssetMapper.selectByAccountSourceIdCoinType(spotAccountId, spotQuoteCoin);
        if (usdtAccountAsset == null) {
            throw new RuntimeException("[quan_account_asset]账户资产未初始化，spotAccountId=" + spotAccountId + "，spotQuoteCoin={}" + spotQuoteCoin);
        }
        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        BeanUtils.copyProperties(usdtAccountAsset, usdt);
        BigDecimal usdtNetBorrow = commContext.getNetBorrow(spotExchangeId, spotAccountId, spotQuoteCoin, true);
        initialSpotUsdt = new SpotUsdt(usdt, usdtNetBorrow);
        // 加载合约账户权益
        QuanAccountFutureAsset futureAsset = quanAccountFutureAssetMapper.selectByAccountSourceIdCoinType(futureAccountId, futureBaseCoin);
        if (futureAsset == null) {
            throw new RuntimeException("[quan_account_future_asset]账户资产未初始化，futureAccountId=" + futureAccountId + "，futureCoinType={}" + futureBaseCoin);
        }
        FutureBalance futureBalance = new FutureBalance();
        BeanUtils.copyProperties(futureAsset, futureBalance);
        BigDecimal futureNetBorrow = commContext.getNetBorrow(futureExchangeId, futureAccountId, futureBaseCoin, true);
        initialFutureRight = new FutureRight(futureBalance, futureNetBorrow);
    }

    /**
     * 加载本次启动盈亏
     */
    private void loadCurrProfit() {
        currSpotCoin = getCurrSpotCoin();
        currSpotUsdt = getCurrSpotUsdt();
        currFutureRight = getCurrFutureRight();
    }

    private SpotCoin getCurrSpotCoin() {
        SpotBalance spotBalance = commContext.getSpotBalance();
        BigDecimal coinNetBorrow = commContext.getNetBorrow(spotExchangeId, spotAccountId, spotBaseCoin, false);
        return new SpotCoin(spotBalance.getCoin(), coinNetBorrow);
    }

    private SpotUsdt getCurrSpotUsdt() {
        SpotBalance spotBalance = commContext.getSpotBalance();
        BigDecimal usdtNetBorrow = commContext.getNetBorrow(spotExchangeId, spotAccountId, spotQuoteCoin, false);
        return new SpotUsdt(spotBalance.getUsdt(), usdtNetBorrow);
    }

    private FutureRight getCurrFutureRight() {
        FutureBalance futureBalance = commContext.getFutureBalance();
        BigDecimal futureNetBorrow = commContext.getNetBorrow(futureExchangeId, futureAccountId, futureBaseCoin, false);
        return new FutureRight(futureBalance, futureNetBorrow);
    }


    /**
     * 获得本次运行盈亏，返回单位为币
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getCurrProfit() {

        SpotCoin spotCoin = getCurrSpotCoin();
        SpotUsdt spotUsdt = getCurrSpotUsdt();
        FutureRight futureRight = getCurrFutureRight();

        BigDecimal coinProfit = spotCoin.getNetBalance().subtract(this.currSpotCoin.getNetBalance());
        BigDecimal usdtProfit = spotUsdt.getNetBalance().subtract(this.currSpotUsdt.getNetBalance()).divide(currPrice, 18, BigDecimal.ROUND_DOWN);
        BigDecimal futureProfit = futureRight.getNetBalance().subtract(this.currFutureRight.getNetBalance());

        return coinProfit.add(usdtProfit).add(futureProfit);
    }

    /**
     * 获得总盈亏
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getTotalProfit() {

        SpotCoin spotCoin = getCurrSpotCoin();
        SpotUsdt spotUsdt = getCurrSpotUsdt();
        FutureRight futureRight = getCurrFutureRight();

        BigDecimal coinProfit = spotCoin.getNetBalance().subtract(initialSpotCoin.getNetBalance());
        BigDecimal usdtProfit = spotUsdt.getNetBalance().subtract(initialSpotUsdt.getNetBalance()).divide(currPrice, 18, BigDecimal.ROUND_DOWN);
        BigDecimal futureProfit = futureRight.getNetBalance().subtract(initialFutureRight.getNetBalance());

        return coinProfit.add(usdtProfit).add(futureProfit);
    }

    @Autowired
    private StrategyRiskHistoryMapper riskHistoryMapper;

    public void saveRiskResult(BigDecimal riskRate, BigDecimal netPosition, RiskMonitor.RiskProfit riskProfit) {
        StrategyRiskHistory strategyRiskHistory = new StrategyRiskHistory();
        strategyRiskHistory.setStrategyName(strategyName);
        strategyRiskHistory.setInstanceConfigId(instanceConfigId);
        strategyRiskHistory.setInstanceId(instanceId);
        strategyRiskHistory.setBaseCoin(futureBaseCoin);
        strategyRiskHistory.setRiskRate(riskRate);
        strategyRiskHistory.setNetPosition(netPosition);
        strategyRiskHistory.setCurrProfit(riskProfit.getCurrProfit());
        strategyRiskHistory.setTotalProfit(riskProfit.getTotalProfit());
        strategyRiskHistory.setCreateTime(new Date());
        strategyRiskHistory.setUpdateTime(new Date());
        riskHistoryMapper.insert(strategyRiskHistory);
        /*boolean isSave = StorageSupport.getInstance("saveRiskResult").checkSavepoint();
        if (isSave) {
            riskHistoryMapper.insert(strategyRiskHistory);
        }*/
    }

    public static class FutureRight {
        private FutureBalance futureBalance;
        private BigDecimal futureNetBorrow;

        public FutureRight(FutureBalance futureBalance, BigDecimal futureNetBorrow) {
            this.futureBalance = futureBalance;
            this.futureNetBorrow = futureNetBorrow;
        }

        private BigDecimal getNetBalance() {
            return futureBalance.getMarginBalance().subtract(futureNetBorrow);
        }
    }

    public static class SpotCoin {
        private SpotBalance.Coin coin;
        private BigDecimal coinNetBorrow;

        public SpotCoin(SpotBalance.Coin coin, BigDecimal coinNetBorrow) {
            this.coin = coin;
            this.coinNetBorrow = coinNetBorrow;
        }

        private BigDecimal getNetBalance() {
            return coin.getTotal().subtract(coinNetBorrow);
        }
    }

    public static class SpotUsdt {
        private SpotBalance.Usdt usdt;
        private BigDecimal usdtNetBorrow;

        public SpotUsdt(SpotBalance.Usdt usdt, BigDecimal usdtNetBorrow) {
            this.usdt = usdt;
            this.usdtNetBorrow = usdtNetBorrow;
        }

        private BigDecimal getNetBalance() {
            return usdt.getTotal().subtract(usdtNetBorrow);
        }
    }


    /**
     * @param orderCtrl 0-正常，1-停止下开仓单，只下平仓单，2-停止合约摆盘，撤销两账户所有未成交订单
     * @param hedgeCtrl 0-正常，2- 停止对冲程序，撤销两账户所有未成交订单
     */
    public void updateRiskCtrl(Integer orderCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureBaseCoin);
        riskConfig.setContractType(this.commContext.getContractTypeFromCode());
        riskConfig.setRiskOrderCtrl(orderCtrl);
        riskConfig.setRiskHedgeCtrl(0);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    public void updateProfitCtrl(Integer orderCtrl, Integer hedgeCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureBaseCoin);
        riskConfig.setContractType(this.commContext.getContractTypeFromCode());
        riskConfig.setProfitOrderCtrl(orderCtrl);
        riskConfig.setProfitHedgeCtrl(hedgeCtrl);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    public void updateNetCtrl(Integer orderCtrl, Integer hedgeCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureBaseCoin);
        riskConfig.setContractType(this.commContext.getContractTypeFromCode());
        riskConfig.setNetOrderCtrl(orderCtrl);
        riskConfig.setNetHedgeCtrl(hedgeCtrl);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    public void setCurrPrice(BigDecimal currPrice) {
        this.currPrice = currPrice;
    }

    public String getSpotBaseCoin() {
        return spotBaseCoin;
    }
}
