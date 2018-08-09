package com.huobi.quantification.strategy;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyHedgeConfigMapper;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dao.StrategyTradeFeeMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.strategy.config.ExchangeConfig;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.DepthBook;
import com.huobi.quantification.strategy.entity.FutureBalance;
import com.huobi.quantification.strategy.entity.FuturePosition;
import com.huobi.quantification.strategy.entity.SpotBalance;
import com.huobi.quantification.strategy.enums.HedgerActionEnum;
import com.huobi.quantification.strategy.enums.OrderActionEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class CommContext {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FutureAccountService futureAccountService;
    @Autowired
    private FutureContractService futureContractService;
    @Autowired
    private SpotMarketService spotMarketService;
    @Autowired
    private SpotOrderService spotOrderService;
    @Autowired
    private FutureOrderService futureOrderService;
    @Autowired
    private StrategyRiskConfigMapper strategyRiskMapper;
    @Autowired
    private StrategyOrderConfigMapper strategyOrderConfigMapper;
    @Autowired
    private StrategyHedgeConfigMapper strategyHedgeConfigMapper;
    @Autowired
    private StrategyTradeFeeMapper strategyTradeFeeMapper;

    private Integer futureExchangeId;
    private Long futureAccountId;
    private String futureBaseCoin;
    private String futureQuoteCoin;
    private String futureContractCode;

    private Integer spotExchangeId;
    private Long spotAccountId;
    private String spotBaseCoin;
    private String spotQuoteCoin;


    private QuanExchangeConfig futureExchangeConfig;

    /********本次期初余额，用于计算净头寸*********/
    private BigDecimal initialSpotUsdt;
    private BigDecimal initialFutureUsdt;

    private BigDecimal exchangeRate;

    public void init(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config spot = group.getSpot();
        StrategyProperties.Config future = group.getFuture();

        this.futureExchangeId = future.getExchangeId();
        this.futureAccountId = future.getAccountId();
        this.futureContractCode = future.getContractCode();
        this.futureBaseCoin = future.getBaseCoin();
        this.futureQuoteCoin = future.getQuotCoin();

        this.spotExchangeId = spot.getExchangeId();
        this.spotAccountId = spot.getAccountId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();

        this.futureExchangeConfig = ExchangeConfig.getExchangeConfig(futureExchangeId, futureBaseCoin, futureQuoteCoin);
        if (futureExchangeConfig == null) {
            throw new RuntimeException("获取期货交易所配置失败，这里需要使用到面值");
        }
        loadInitialUsdt();
    }

    private void loadInitialUsdt() {
        initialSpotUsdt = getCurrSpotUsdt();
        logger.info("币币账户期初余额：{}Usdt", initialSpotUsdt);
        initialFutureUsdt = getCurrFutureUsdt();
        logger.info("合约账户期初净空仓金额：{}Usdt", initialFutureUsdt);
    }

    public boolean cancelAllSpotOrder() {
        SpotCancleAllOrderReqDto req = new SpotCancleAllOrderReqDto();
        req.setExchangeId(spotExchangeId);
        req.setAccountId(spotAccountId);
        req.setBaseCoin(spotBaseCoin);
        req.setQuoteCoin(spotQuoteCoin);
        try {
            ServiceResult result = spotOrderService.cancelAllOrder(req);
            if (result.isSuccess()) {
                logger.info("对冲取消所有现货订单成功，spotExchangeId={}，spotAccountId={}，spotBaseCoin={}，spotQuoteCoin={}",
                        spotExchangeId, spotAccountId, spotBaseCoin, spotQuoteCoin);
                return true;
            }
        } catch (Exception e) {
            logger.info("取消现货订单失败", e);
        }
        return false;
    }

    public boolean cancelAllFutureOrder() {
        FutureCancelAllOrderReqDto reqDto = new FutureCancelAllOrderReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setAccountId(spotAccountId);
        reqDto.setSymbol(spotBaseCoin.toUpperCase());
        ServiceResult result = futureOrderService.cancelAllOrder(reqDto);
        if (result.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    public BigDecimal getNetPositionUsdt() {
        // 币币账户期末余额USDT
        BigDecimal currSpotUsdt = getCurrSpotUsdt();
        // 合约账户期末净空仓金额USDT
        BigDecimal currFutureUsdt = getCurrFutureUsdt();
        BigDecimal netPosition = currSpotUsdt.subtract(initialSpotUsdt).add(currFutureUsdt.subtract(initialFutureUsdt));
        logger.info("当前净头寸：{}Usdt", netPosition);
        return netPosition;
    }

    private BigDecimal getCurrSpotUsdt() {
        SpotBalance spotBalance = getSpotBalance();
        SpotBalance.Usdt usdt = spotBalance.getUsdt();
        return usdt.getAvailable();
    }

    /**
     * 合约账户净空仓金额USDT记为M2
     *
     * @return
     */
    public BigDecimal getCurrFutureUsdt() {
        BigDecimal longAmount;
        BigDecimal shortAmount;
        FuturePosition futurePosition = getFuturePosition();
        FuturePosition.Position longPosi = futurePosition.getLongPosi();
        if (longPosi == null) {
            longAmount = BigDecimal.ZERO;
        } else {
            longAmount = longPosi.getAmount();
        }
        FuturePosition.Position shortPosi = futurePosition.getShortPosi();
        if (shortPosi == null) {
            shortAmount = BigDecimal.ZERO;
        } else {
            shortAmount = shortPosi.getAmount();
        }
        exchangeRate = getExchangeRateOfUSDT2USD();
        if (exchangeRate == null) {
            throw new RuntimeException("获取USDT2USD汇率失败");
        }
        logger.info("计算合约账户净空仓金额，空仓：{}，多仓：{}，汇率：{}，面值：{}", shortAmount, longAmount, exchangeRate, futureExchangeConfig.getFaceValue());
        return shortAmount.subtract(longAmount)
                .multiply(futureExchangeConfig.getFaceValue())
                .divide(exchangeRate, 18, BigDecimal.ROUND_DOWN);
    }

    public DepthBook getDepth() {
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setBaseCoin(spotBaseCoin);
        reqDto.setQuoteCoin(spotQuoteCoin);
        ServiceResult<SpotDepthRespDto> result = spotMarketService.getDepth(reqDto);
        if (result.isSuccess()) {
            SpotDepthRespDto.DataBean data = result.getData().getData();
            DepthBook depthBook = new DepthBook();
            data.getAsks().forEach(e -> {
                depthBook.getAsks().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
            });
            data.getBids().forEach(e -> {
                depthBook.getBids().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
            });
            return depthBook;
        } else {
            throw new RuntimeException("获取火币现货深度异常");
        }
    }

    public BigDecimal getExchangeRateOfUSDT2USD() {
        for (int i = 0; i < 3; i++) {
            ServiceResult<BigDecimal> result = futureContractService.getExchangeRateOfUSDT2USD();
            if (result.isSuccess()) {
                logger.info("获取USDT2USD汇率成功");
                return result.getData();
            } else {
                continue;
            }
        }
        logger.error("获取USDT2USD汇率异常");
        return null;
    }

    public FuturePosition getFuturePosition() {
        FuturePositionReqDto reqDto = new FuturePositionReqDto();
        reqDto.setExchangeId(this.futureExchangeId);
        reqDto.setAccountId(this.futureAccountId);
        reqDto.setCoinType(this.futureBaseCoin);
        ServiceResult<FuturePositionRespDto> result = futureAccountService.getPosition(reqDto);
        if (result.isSuccess()) {
            Map<String, List<FuturePositionRespDto.Position>> dataMap = result.getData().getDataMap();
            FuturePosition futurePosition = new FuturePosition();
            // 如果为null，代表当前账户没有持仓
            if (dataMap != null && CollectionUtils.isNotEmpty(dataMap.get(this.futureBaseCoin))) {
                List<FuturePositionRespDto.Position> positionList = dataMap.get(this.futureBaseCoin);
                positionList.stream().forEach(e -> {
                    if (e.getContractCode().equalsIgnoreCase(this.futureContractCode) && e.getOffset() == OffsetEnum.LONG.getOffset()) {
                        FuturePosition.Position longPosi = new FuturePosition.Position();
                        BeanUtils.copyProperties(e, longPosi);
                        futurePosition.setLongPosi(longPosi);
                    }

                    if (e.getContractCode().equalsIgnoreCase(this.futureContractCode) && e.getOffset() == OffsetEnum.SHORT.getOffset()) {
                        FuturePosition.Position shortPosi = new FuturePosition.Position();
                        BeanUtils.copyProperties(e, shortPosi);
                        futurePosition.setShortPosi(shortPosi);
                    }
                });
                logger.info("获取期货持仓信息成功，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureBaseCoin);
                return futurePosition;
            } else {
                logger.info("获取期货持仓信息成功，但当前账户没有持仓，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureBaseCoin);
                return futurePosition;
            }
        } else {
            throw new RuntimeException("获取期货持仓信息失败，exchangeId=" + futureExchangeId + "，futureAccountId=" + futureAccountId + "，futureCoinType=" + futureBaseCoin + ",失败原因={}" + result.getMessage());
        }
    }

    public FutureBalance getFutureBalance() {
        try {
            FutureBalanceReqDto reqDto = new FutureBalanceReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setCoinType(this.futureBaseCoin);
            ServiceResult<FutureBalanceRespDto> result = futureAccountService.getBalance(reqDto);
            if (result.isSuccess()) {
                FutureBalance futureBalance = new FutureBalance();
                Map<String, FutureBalanceRespDto.DataBean> dataMap = result.getData().getData();
                if (dataMap != null && dataMap.get(futureBaseCoin) != null) {
                    FutureBalanceRespDto.DataBean dataBean = dataMap.get(futureBaseCoin);
                    BeanUtils.copyProperties(dataBean, futureBalance);
                    logger.info("获取期货资产信息成功，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureBaseCoin);
                    return futureBalance;
                }else {
                    logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureBaseCoin);
                    return null;
                }
            } else {
                logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}，失败原因={}", this.futureExchangeId, futureAccountId, futureBaseCoin, result.getMessage());
                return null;
            }
        } catch (BeansException e) {
            logger.error("获取期货资产信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", this.futureExchangeId, futureAccountId, futureBaseCoin, e);
            return null;
        }
    }

    @Autowired
    private SpotAccountService spotAccountService;

    public SpotBalance getSpotBalance() {
        logger.info("获取现货资产信息成功");
        return SpotBalanceMock.getSpotBalance();
        /*SpotBalanceReqDto reqDto = new SpotBalanceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setAccountId(spotAccountId);
        ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(reqDto);
        SpotBalance spotBalance = new SpotBalance();
        if (result.isSuccess()) {
            SpotBalanceRespDto.DataBean coinBean = result.getData().getData().get(spotBaseCoin);
            SpotBalance.Coin coin = new SpotBalance.Coin();
            BeanUtils.copyProperties(coinBean, coin);
            spotBalance.setCoin(coin);

            SpotBalanceRespDto.DataBean usdtBean = result.getData().getData().get(spotQuoteCoin);
            SpotBalance.Usdt usdt = new SpotBalance.Usdt();
            BeanUtils.copyProperties(usdtBean, usdt);
            spotBalance.setUsdt(usdt);
            return spotBalance;
        } else {
            return null;
        }*/
    }

    public BigDecimal getSpotCurrentPrice() {
        SpotCurrentPriceReqDto reqDto = new SpotCurrentPriceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setBaseCoin(spotBaseCoin);
        reqDto.setQuoteCoin(spotQuoteCoin);
        try {
            ServiceResult<SpotCurrentPriceRespDto> currentPrice = spotMarketService.getCurrentPrice(reqDto);
            if (currentPrice.isSuccess()) {
                BigDecimal price = currentPrice.getData().getCurrentPrice();
                logger.info("获取当前价格成功,price={}，exchangeId={}，futureBaseCoin={}，futureQuoteCoin={}", price, futureExchangeId, futureBaseCoin, futureQuoteCoin);
                return price;
            }
        } catch (Exception e) {
            logger.error("获取当前价格失败，exchangeId={}，futureBaseCoin={}，futureQuoteCoin={}", futureExchangeId, futureBaseCoin, futureQuoteCoin);
        }
        return null;
    }

    public boolean isThisWeek() {
        return "this_week".equals(getContractTypeFromCode());
    }

    public String getContractTypeFromCode() {
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


    public StrategyTradeFee getStrategyTradeFeeConfig() {
        String contractType = getContractTypeFromCode();
        StrategyTradeFee tradeFeeConfig = strategyTradeFeeMapper.selectBySymbolContractType(futureBaseCoin, contractType);
        if (tradeFeeConfig != null) {
            logger.info("获取交易费用策略参数：" + JSON.toJSONString(tradeFeeConfig));
        }
        return tradeFeeConfig;
    }

    public StrategyOrderConfig getStrategyOrderConfig() {
        String contractType = getContractTypeFromCode();
        StrategyOrderConfig orderConfig = strategyOrderConfigMapper.selectBySymbolContractType(futureBaseCoin, contractType);
        if (orderConfig != null) {
            logger.info("获取订单策略参数：" + JSON.toJSONString(orderConfig));
        }
        return orderConfig;
    }

    public StrategyHedgeConfig getStrategyHedgeConfig() {
        String contractType = getContractTypeFromCode();
        StrategyHedgeConfig hedgeConfig = strategyHedgeConfigMapper.selectBySymbolContractType(futureBaseCoin, contractType);
        return hedgeConfig;
    }

    public StrategyRiskConfig getStrategyRiskConfig() {
        String contractType = getContractTypeFromCode();
        StrategyRiskConfig riskConfig = strategyRiskMapper.selectBySymbolContractType(futureBaseCoin, contractType);
        if (riskConfig != null) {
            logger.info("获取风控策略参数：" + JSON.toJSONString(riskConfig));
        }
        return riskConfig;
    }

    public OrderActionEnum getOrderAction() {
        String contractType = getContractTypeFromCode();
        int orderAction = strategyRiskMapper.selectOrderAction(futureBaseCoin, contractType);
        return OrderActionEnum.valueOf(orderAction);
    }

    public HedgerActionEnum getHedgeAction() {
        String contractType = getContractTypeFromCode();
        int action = strategyRiskMapper.selectHedgeAction(futureBaseCoin, contractType);
        return HedgerActionEnum.valueOf(action);
    }
}
