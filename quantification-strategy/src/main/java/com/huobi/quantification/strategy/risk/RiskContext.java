package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dao.StrategyFinanceHistoryMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.ContractCodeDto;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.order.entity.FutureBalance;
import com.huobi.quantification.strategy.order.entity.SpotBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class RiskContext {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FutureAccountService futureAccountService;
    @Autowired
    private SpotAccountService spotAccountService;
    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private SpotOrderService spotOrderService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private StrategyRiskConfigMapper strategyRiskMapper;
    @Autowired
    private QuanAccountHistoryMapper quanAccountHistoryMapper;
    @Autowired
    private FutureContractService futureContractService;

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
    private FutureRights futureRights;
    private SpotCoin spotCoin;
    private SpotUsdt spotUsdt;

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

        /*spotAccountService.saveFirstBalance(spotAccountId, spotExchangeId);
        futureAccountService.saveAccountsInfo(futureAccountId, contractCode);
        spotAccountService.saveFirstDebit(hashMap, future.getContractCode());*/
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
        SpotBalance spotBalance = getSpotBalance();
        BigDecimal coinAvailable = spotBalance.getCoin().getAvailable();
        BigDecimal usdtAvailable = spotBalance.getUsdt().getAvailable();
        FutureBalance futureBalance = getFutureBalance();
        BigDecimal futureRight = futureBalance.getMarginBalance();

        BigDecimal spotBaseCoinNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotBaseCoin, false);
        BigDecimal usdtNetBorrow = getNetBorrow(spotExchangeId, spotAccountId, spotQuoteCoin, false);
        BigDecimal futureNetBorrow = getNetBorrow(futureExchangeId, futureAccountId, futureCoinType, false);

        BigDecimal coinProfit = coinAvailable.subtract(spotBaseCoinNetBorrow).subtract(spotCoin.getNetBalance());
        BigDecimal usdtProfit = usdtAvailable.subtract(usdtNetBorrow).subtract(spotUsdt.getNetBalance()).divide(currPrice, 18, BigDecimal.ROUND_DOWN);
        BigDecimal futureProfit = futureRight.subtract(futureNetBorrow).subtract(futureRights.getNetBalance());

        return coinProfit.add(usdtProfit).add(futureProfit);
    }

    /**
     * 获得总盈亏
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getTotalProfit() {


        return null;
    }

    public static class FutureRights {
        private FutureBalance futureBalance;
        private BigDecimal futureNetBorrow;

        public FutureRights(FutureBalance futureBalance, BigDecimal futureNetBorrow) {
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



    @Autowired
    private StrategyFinanceHistoryMapper financeHistoryMapper;

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

    public FutureBalance getFutureBalance() {
        try {
            FutureBalanceReqDto reqDto = new FutureBalanceReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setCoinType(this.futureCoinType);
            ServiceResult<FutureBalanceRespDto> result = futureAccountService.getBalance(reqDto);
            if (result.isSuccess()) {
                Map<String, FutureBalanceRespDto.DataBean> data = result.getData().getData();
                FutureBalanceRespDto.DataBean dataBean = data.get(futureCoinType);
                if (dataBean == null) {
                    dataBean = data.get(futureCoinType.toUpperCase());
                }
                if (dataBean != null) {
                    FutureBalance futureBalance = new FutureBalance();
                    BeanUtils.copyProperties(dataBean, futureBalance);
                    logger.info("获取期货资产信息成功，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureCoinType);
                    return futureBalance;
                } else {
                    logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureCoinType);
                    return null;
                }
            } else {
                logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}，失败原因={}", this.futureExchangeId, futureAccountId, futureCoinType, result.getMessage());
                return null;
            }
        } catch (BeansException e) {
            logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureCoinType, e);
            return null;
        }
    }

    public SpotBalance getSpotBalance() {
        SpotBalance spotBalance = new SpotBalance();

        SpotBalance.Coin coin = new SpotBalance.Coin();
        coin.setAvailable(BigDecimal.valueOf(9999999999999999L));
        spotBalance.setCoin(coin);

        SpotBalance.Usdt usdt = new SpotBalance.Usdt();
        usdt.setAvailable(BigDecimal.valueOf(9999999999999999L));
        spotBalance.setUsdt(usdt);
        return spotBalance;

        /*SpotBalance spotBalance = new SpotBalance();
        try {
            ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(null);
            Map<String, SpotBalanceRespDto.DataBean> data = balance.getData().getData();
            SpotBalanceRespDto.DataBean dataBean = data.get(futureCoinType);
            if (dataBean != null) {
                SpotBalance.Coin coin = new SpotBalance.Coin();
                BeanUtils.copyProperties(dataBean, coin);
                spotBalance.setCoin(coin);
            }
            SpotBalanceRespDto.DataBean usdtBean = data.get("usdt");
            if (usdtBean != null) {
                SpotBalance.Usdt usdt = new SpotBalance.Usdt();
                BeanUtils.copyProperties(dataBean, usdt);
                spotBalance.setUsdt(usdt);
            }
            return spotBalance;
        } catch (BeansException e) {
            logger.error("获取现货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", exchangeId, futureAccountId, futureCoinType, e);
            return null;
        }*/
    }
}
