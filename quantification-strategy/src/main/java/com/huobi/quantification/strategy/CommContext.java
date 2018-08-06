package com.huobi.quantification.strategy;


import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.enums.OffsetEnum;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.entity.DepthBook;
import com.huobi.quantification.strategy.entity.FutureBalance;
import com.huobi.quantification.strategy.entity.FuturePosition;
import com.huobi.quantification.strategy.entity.SpotBalance;
import com.huobi.quantification.strategy.risk.RiskContext;
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

    private BigDecimal faceValue = BigDecimal.valueOf(100);

    private Integer futureExchangeId;
    private Long futureAccountId;
    private Integer futureLever;
    private String futureContractType;
    private String futureContractCode;
    private String futureBaseCoin;
    private String futureQuoteCoin;
    private String futureCoinType;

    private Integer spotExchangeId;
    private Long spotAccountId;
    private String spotBaseCoin;
    private String spotQuoteCoin;

    private BigDecimal exchangeRate = BigDecimal.ONE;

    /**
     * 币币账户期初余额USDT
     */
    private BigDecimal initialSpotUsd;
    /**
     * 合约账户期初净空仓金额USD
     */
    private BigDecimal initialFutureUsd;

    public void init(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config spot = group.getSpot();
        StrategyProperties.Config future = group.getFuture();

        this.spotExchangeId = spot.getExchangeId();
        this.spotAccountId = spot.getAccountId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();

        this.futureExchangeId = future.getExchangeId();
        this.futureAccountId = future.getAccountId();
        this.futureLever = future.getLever();
        this.futureContractCode = future.getContractCode();
        this.futureContractType = getContractTypeFromCode();
        this.futureBaseCoin = future.getBaseCoin();
        this.futureQuoteCoin = future.getQuotCoin();
        this.futureCoinType = future.getBaseCoin();
        loadInitialUsdt();
    }

    private void loadInitialUsdt() {
        initialSpotUsd = getCurrSpotUsd();
        initialFutureUsd = getCurrFutureUsd();
    }

    public boolean cancelAllSpotOrder() {
        SpotCancleAllOrderReqDto req = new SpotCancleAllOrderReqDto();
        req.setExchangeId(spotExchangeId);
        req.setAccountId(spotAccountId);
        req.setBaseCoin(spotBaseCoin);
        req.setQuoteCoin(spotQuoteCoin);
        ServiceResult result = spotOrderService.cancelAllOrder(req);
        if (result.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean cancelAllFutureOrder() {

        return true;
    }

    public BigDecimal getNetPosition() {
        BigDecimal currSpotUsd = getCurrSpotUsd();
        BigDecimal currFutureUsd = getCurrFutureUsd();
        BigDecimal netPosition = currSpotUsd.subtract(initialSpotUsd).add(currFutureUsd.subtract(initialFutureUsd));
        return netPosition;
    }

    private BigDecimal getCurrSpotUsd() {
        SpotBalance spotBalance = getSpotBalance();
        SpotBalance.Usdt usdt = spotBalance.getUsdt();
        exchangeRate = getExchangeRateOfUSDT2USD();
        return usdt.getAvailable().multiply(exchangeRate);
    }

    /**
     * 净持仓金额M2
     *
     * @return
     */
    public BigDecimal getCurrFutureUsd() {
        FuturePosition futurePosition = getFuturePosition();
        FuturePosition.Position longPosi = futurePosition.getLongPosi();
        FuturePosition.Position shortPosi = futurePosition.getShortPosi();
        return shortPosi.getAmount().subtract(longPosi.getAmount()).multiply(faceValue);
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
                logger.error("获取USDT2USD汇率成功");
                return result.getData();
            } else {
                continue;
            }
        }
        logger.error("获取USDT2USD汇率异常");
        return null;
    }

    public FuturePosition getFuturePosition() {
        try {
            FuturePositionReqDto reqDto = new FuturePositionReqDto();
            reqDto.setExchangeId(this.futureExchangeId);
            reqDto.setAccountId(this.futureAccountId);
            reqDto.setCoinType(this.futureCoinType);
            ServiceResult<FuturePositionRespDto> result = futureAccountService.getPosition(reqDto);
            if (result.isSuccess()) {
                Map<String, List<FuturePositionRespDto.Position>> dataMap = result.getData().getDataMap();
                List<FuturePositionRespDto.Position> positionList = dataMap.get(this.futureCoinType);
                FuturePosition futurePosition = new FuturePosition();
                // 如果为null，代表当前账户没有持仓
                if (CollectionUtils.isNotEmpty(positionList)) {
                    positionList.stream().forEach(e -> {
                        if (e.getContractType().equalsIgnoreCase(futureContractType) && e.getOffset() == OffsetEnum.LONG.getOffset()) {
                            FuturePosition.Position longPosi = new FuturePosition.Position();
                            BeanUtils.copyProperties(e, longPosi);
                            futurePosition.setLongPosi(longPosi);
                        }

                        if (e.getContractType().equalsIgnoreCase(futureContractType) && e.getOffset() == OffsetEnum.SHORT.getOffset()) {
                            FuturePosition.Position shortPosi = new FuturePosition.Position();
                            BeanUtils.copyProperties(e, shortPosi);
                            futurePosition.setShortPosi(shortPosi);
                        }
                    });
                    logger.info("获取期货持仓信息成功，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureCoinType);
                    return futurePosition;
                } else {
                    logger.info("获取期货持仓信息成功，但当前账户没有持仓，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureCoinType);
                    return futurePosition;
                }
            } else {
                logger.error("获取期货持仓信息失败，exchangeId={}，futureAccountId={}，futureCoinType={},失败原因={}", futureExchangeId, futureAccountId, futureCoinType, result.getMessage());
                return null;
            }
        } catch (Exception e) {
            logger.error("获取期货持仓信息失败，exchangeId={}，futureAccountId={}，futureCoinType={}", futureExchangeId, futureAccountId, futureCoinType, e);
            return null;
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

    @Autowired
    private SpotAccountService spotAccountService;

    public SpotBalance getSpotBalance() {
        SpotBalanceReqDto reqDto = new SpotBalanceReqDto();
        reqDto.setExchangeId(spotExchangeId);
        reqDto.setAccountId(spotAccountId);
        ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(reqDto);
        SpotBalance spotBalance = new SpotBalance();
        if (result.isSuccess()) {
            SpotBalanceRespDto.DataBean coinBean = result.getData().getData().get(spotBaseCoin);
            SpotBalance.Coin coin = new SpotBalance.Coin();
            BeanUtils.copyProperties(coinBean, coin);
            coin.setAvailable(BigDecimal.valueOf(999999999));
            spotBalance.setCoin(coin);

            SpotBalanceRespDto.DataBean usdtBean = result.getData().getData().get(spotQuoteCoin);
            SpotBalance.Usdt usdt = new SpotBalance.Usdt();
            BeanUtils.copyProperties(usdtBean, usdt);
            usdt.setAvailable(BigDecimal.valueOf(999999999));
            spotBalance.setUsdt(usdt);
            return spotBalance;
        } else {
            return null;
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
                logger.error("获取当前价格成功，exchangeId={}，futureBaseCoin={}，futureQuoteCoin={}", futureExchangeId, futureBaseCoin, futureQuoteCoin);
                return currentPrice.getData().getCurrentPrice();
            }
        } catch (Exception e) {
            logger.error("获取当前价格失败，exchangeId={}，futureBaseCoin={}，futureQuoteCoin={}", futureExchangeId, futureBaseCoin, futureQuoteCoin);
        }
        return null;
    }

    public boolean isThisWeek() {
        return "this_week".equals(futureContractType);
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
}
