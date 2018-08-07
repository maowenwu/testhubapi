package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.StrategyFinanceHistoryMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.FutureBalance;
import com.huobi.quantification.strategy.entity.SpotBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RiskContext {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FutureAccountService futureAccountService;
    @Autowired
    private StrategyFinanceHistoryMapper financeHistoryMapper;
    @Autowired
    private SpotAccountService spotAccountService;
    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private StrategyRiskConfigMapper strategyRiskMapper;
    @Autowired
    private FutureContractService futureContractService;
    @Autowired
    private QuanAccountAssetMapper quanAccountAssetMapper;
    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    private Integer futureExchangeId;
    private Long futureAccountId;
    private String futureCoinType;
    private String futureSymbol;
    private String futureContractType;
    private String futureContractCode;

    private Long spotAccountId;
    private Integer spotExchangeId;
    private String spotBaseCoin;
    private String spotQuoteCoin;


    private BigDecimal currPrice;

    private SpotCoin currSpotCoin;
    private SpotUsdt currSpotUsdt;
    private FutureRight currFutureRight;

    private SpotCoin initialSpotCoin;
    private SpotUsdt initialSpotUsdt;
    private FutureRight initialFutureRight;


    public void init(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config future = group.getFuture();
        StrategyProperties.Config spot = group.getSpot();

        this.futureExchangeId = future.getExchangeId();
        this.futureAccountId = future.getAccountId();
        this.futureCoinType = future.getBaseCoin();
        this.futureSymbol = future.getBaseCoin();
        this.futureContractCode = future.getContractCode();
        this.futureContractType = getContractTypeFromCode();

        this.spotExchangeId = spot.getExchangeId();
        this.spotAccountId = spot.getAccountId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();

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
        BigDecimal coinNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotBaseCoin, true);
        initialSpotCoin = new SpotCoin(coin, coinNetBorrow);
        // 加载币币账户usdt期初余额
        QuanAccountAsset usdtAccountAsset = quanAccountAssetMapper.selectByAccountSourceIdCoinType(spotAccountId, spotQuoteCoin);
        if (usdtAccountAsset == null) {
            throw new RuntimeException("[quan_account_asset]账户资产未初始化，spotAccountId=" + spotAccountId + "，spotQuoteCoin={}" + spotQuoteCoin);
        }
        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        BeanUtils.copyProperties(usdtAccountAsset, usdt);
        BigDecimal usdtNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotQuoteCoin, true);
        initialSpotUsdt = new SpotUsdt(usdt, usdtNetBorrow);
        // 加载合约账户权益
        QuanAccountFutureAsset futureAsset = quanAccountFutureAssetMapper.selectByAccountSourceIdCoinType(futureAccountId, futureCoinType);
        if (futureAsset == null) {
            throw new RuntimeException("[quan_account_future_asset]账户资产未初始化，futureAccountId=" + futureAccountId + "，futureCoinType={}" + futureCoinType);
        }
        FutureBalance futureBalance = new FutureBalance();
        BeanUtils.copyProperties(futureAsset, futureBalance);
        BigDecimal futureNetBorrow = getNetBorrow(futureExchangeId, futureAccountId, futureCoinType, true);
        initialFutureRight = new FutureRight(futureBalance, futureNetBorrow);
    }


    private void loadCurrProfit() {
        currSpotCoin = getCurrSpotCoin();
        currSpotUsdt = getCurrSpotUsdt();
        currFutureRight = getCurrFutureRight();
    }

    private SpotCoin getCurrSpotCoin() {
        SpotBalanceReqDto reqDto = new SpotBalanceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setAccountId(spotAccountId);
        reqDto.setCoinType(spotBaseCoin);
        ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(reqDto);
        SpotBalance.Coin coin = new SpotBalance.Coin();
        if (result.isSuccess()) {
            SpotBalanceRespDto.DataBean dataBean = result.getData().getData().get(spotBaseCoin);
            BeanUtils.copyProperties(dataBean, coin);
        }
        BigDecimal coinNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotBaseCoin, false);
        return new SpotCoin(coin, coinNetBorrow);
    }

    private SpotUsdt getCurrSpotUsdt() {
        SpotBalanceReqDto reqDto = new SpotBalanceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setAccountId(spotAccountId);
        reqDto.setCoinType(spotQuoteCoin);
        ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(reqDto);
        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        if (result.isSuccess()) {
            SpotBalanceRespDto.DataBean dataBean = result.getData().getData().get(spotQuoteCoin);
            BeanUtils.copyProperties(dataBean, usdt);
        }
        BigDecimal usdtNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotQuoteCoin, false);
        return new SpotUsdt(usdt, usdtNetBorrow);
    }

    private FutureRight getCurrFutureRight() {
        FutureBalanceReqDto reqDto = new FutureBalanceReqDto();
        reqDto.setExchangeId(futureExchangeId);
        reqDto.setAccountId(futureAccountId);
        reqDto.setCoinType(futureCoinType);
        ServiceResult<FutureBalanceRespDto> result = futureAccountService.getBalance(reqDto);
        FutureBalance futureBalance = new FutureBalance();
        if (result.isSuccess()) {
            FutureBalanceRespDto.DataBean dataBean = result.getData().getData().get(futureCoinType);
            BeanUtils.copyProperties(dataBean, futureBalance);
        }
        BigDecimal futureNetBorrow = getNetBorrow(futureExchangeId, futureAccountId, futureCoinType, false);
        return new FutureRight(futureBalance, futureNetBorrow);
    }


    /**
     * 根据contractCode获取对应coin的保证金率
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getRiskRate() {
        FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
        balanceReqDto.setExchangeId(futureExchangeId);
        balanceReqDto.setAccountId(futureAccountId);
        balanceReqDto.setCoinType(futureCoinType);
        ServiceResult<FutureBalanceRespDto> result = futureAccountService.getBalance(balanceReqDto);
        if (result.isSuccess()) {
            return result.getData().getData().get(futureCoinType).getRiskRate();
        } else {
            logger.error("取不到期货账户余额，错误信息：{}", result.getMessage());
            throw new RuntimeException("取不到期货账户余额，错误信息：" + result.getMessage());
        }
    }


    /**
     * 获得本次运行盈亏
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
            return coin.getAvailable().subtract(coinNetBorrow);
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
            return usdt.getAvailable().subtract(usdtNetBorrow);
        }
    }


    private BigDecimal getNetBorrow(int exchangeId, Long accountId, String coinType, boolean initialOnly) {
        BigDecimal netBorrow = financeHistoryMapper.getNetBorrow(exchangeId, accountId, coinType, initialOnly);
        if (netBorrow == null) {
            return BigDecimal.ZERO;
        } else {
            return netBorrow;
        }
    }


    /**
     * 调用接口方法获取当前账号组净头寸，并返回
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getNetPosition() {

        return BigDecimal.ZERO;
    }


    public StrategyRiskConfig getStrategyRiskConfig() {
        return strategyRiskMapper.selectByPrimaryKey(1);
    }

    /**
     * @param orderCtrl 0-正常，1-停止下开仓单，只下平仓单，2-停止合约摆盘，撤销两账户所有未成交订单
     * @param hedgeCtrl 0-正常，2- 停止对冲程序，撤销两账户所有未成交订单
     */
    public void updateRiskCtrl(Integer orderCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureSymbol);
        riskConfig.setContractType(this.futureContractType);
        riskConfig.setRiskOrderCtrl(orderCtrl);
        riskConfig.setRiskHedgeCtrl(0);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    public void updateProfitCtrl(Integer orderCtrl, Integer hedgeCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureSymbol);
        riskConfig.setContractType(this.futureContractType);
        riskConfig.setProfitOrderCtrl(orderCtrl);
        riskConfig.setProfitHedgeCtrl(hedgeCtrl);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    public void updateNetCtrl(Integer orderCtrl, Integer hedgeCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureSymbol);
        riskConfig.setContractType(this.futureContractType);
        riskConfig.setNetOrderCtrl(orderCtrl);
        riskConfig.setNetHedgeCtrl(hedgeCtrl);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }

    private String getContractTypeFromCode() {
        try {
            ServiceResult<ContractCodeDto> result = futureContractService.getContractCode(futureExchangeId, futureContractCode);
            if (result.isSuccess()) {
                return result.getData().getContractType();
            } else {
                throw new RuntimeException("初始化异常，获取ContractType失败，请检查是否未启动定时任务");
            }
        } catch (Throwable e) {
            throw new RuntimeException("初始化异常，获取ContractType失败，请检查是否未启动定时任务", e);
        }
    }


    public BigDecimal getSpotCurrentPrice() {
        SpotCurrentPriceReqDto reqDto = new SpotCurrentPriceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setBaseCoin(spotBaseCoin);
        reqDto.setQuoteCoin(spotQuoteCoin);
        try {
            ServiceResult<SpotCurrentPriceRespDto> currentPrice = spotMarketService.getCurrentPrice(reqDto);
            if (currentPrice.isSuccess()) {
                logger.info("获取当前价格成功，spotExchangeId={}，spotBaseCoin={}，futureQuoteCoin={}", spotExchangeId, spotBaseCoin, spotQuoteCoin);
                return currentPrice.getData().getCurrentPrice();
            }
        } catch (Exception e) {
            logger.error("获取当前价格失败，spotExchangeId={}，spotBaseCoin={}，futureQuoteCoin={}", spotExchangeId, spotBaseCoin, spotQuoteCoin, e);
        }
        return null;
    }

    public void setCurrPrice(BigDecimal currPrice) {
        this.currPrice = currPrice;
    }
}
