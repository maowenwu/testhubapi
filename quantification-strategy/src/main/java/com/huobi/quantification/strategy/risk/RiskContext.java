package com.huobi.quantification.strategy.risk;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dao.StrategyRiskConfigMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountHistory;
import com.huobi.quantification.entity.StrategyRiskConfig;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;
import com.huobi.quantification.strategy.hedging.service.CommonService;
import com.huobi.quantification.strategy.risk.entity.FutureBalance;
import com.huobi.quantification.strategy.risk.entity.SpotBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Scope("prototype")
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

    public void init(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config future = group.getFuture();
        StrategyProperties.Config spot = group.getSpot();
        this.futureExchangeId = future.getExchangeId();
        this.futureAccountId = future.getAccountId();
        this.futureCoinType = future.getBaseCoin();
        this.futureContractCode = future.getContractCode();
        this.spotAccountId = spot.getAccountId();
        this.spotExchangeId = spot.getExchangeId();
        this.spotBaseCoin = spot.getBaseCoin();
        this.spotQuoteCoin = spot.getQuotCoin();
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
     * 获得总盈亏
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getTotalProfit() {
        //取期货账户信息
        QuanAccountHistory futureFirstBalance = quanAccountHistoryMapper.getFirstBalance(futureAccountId, futureExchangeId, futureCoinType);
        FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
        balanceReqDto.setAccountId(futureAccountId);
        balanceReqDto.setCoinType(futureCoinType);
        balanceReqDto.setExchangeId(futureExchangeId);
        ServiceResult<FutureBalanceRespDto> end = futureAccountService.getBalance(balanceReqDto);
        if (end.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
            logger.error("取不到期货用户当前余额，错误信息{}:", end.getMessage());
        }
        FutureBalance endFuture = new FutureBalance();
        endFuture.setMarginBalance(end.getData().getData().get(futureCoinType).getMarginBalance());
        //取现货账户信息
        SpotBalanceReqDto balanceReqDto2 = new SpotBalanceReqDto();
        balanceReqDto2.setAccountId(spotAccountId);
        balanceReqDto2.setExchangeId(futureExchangeId);
        ServiceResult<SpotBalanceRespDto> end2 = spotAccountService.getBalance(balanceReqDto2);
        if (end2.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
            logger.error("取不到现货用户当前余额，错误信息：{}", end2.getMessage());
        }
        SpotBalance startSpotCoin1 = new SpotBalance();
        SpotBalance startSpotCoin2 = new SpotBalance();
        SpotBalance endSpotCoin1 = new SpotBalance();
        SpotBalance endSpotCoin2 = new SpotBalance();
        endSpotCoin1.setTotal(end2.getData().getData().get(spotBaseCoin).getTotal());
        endSpotCoin2.setTotal(end2.getData().getData().get(spotQuoteCoin).getTotal());
        QuanAccountHistory firstBalanceCoin1 = quanAccountHistoryMapper.getFirstBalance(spotAccountId, spotExchangeId, spotBaseCoin);
        QuanAccountHistory firstBalanceCoin2 = quanAccountHistoryMapper.getFirstBalance(spotAccountId, spotExchangeId, spotQuoteCoin);
        startSpotCoin1.setTotal(firstBalanceCoin1.getCurrentAmount());
        startSpotCoin2.setTotal(firstBalanceCoin2.getCurrentAmount());
        /*BigDecimal endDebitCoin1 = getEndDebit(spotBaseCoin, spotExchangeId, spotAccountId);
        BigDecimal endDebitcoin2 = getEndDebit(spotQuoteCoin, spotExchangeId, spotAccountId);
        BigDecimal endDebitFuture = getEndDebit(futureCoinType, futureExchangeId, futureAccountId);*/
        SpotCurrentPriceReqDto currentPriceReqDto = new SpotCurrentPriceReqDto();
        currentPriceReqDto.setBaseCoin(spotBaseCoin);
        currentPriceReqDto.setQuoteCoin(spotQuoteCoin);
        currentPriceReqDto.setExchangeId(spotExchangeId);
        ServiceResult<SpotCurrentPriceRespDto> currentPriceResp = spotMarketService.getCurrentPrice(currentPriceReqDto);
        BigDecimal currentPrice = currentPriceResp.getData().getCurrentPrice();
       /* BigDecimal number1 = endSpotCoin1.getTotal().subtract(endDebitCoin1).subtract(startSpotCoin1.getTotal());
        BigDecimal number2 = endSpotCoin2.getTotal().subtract(endDebitcoin2).subtract(startSpotCoin2.getTotal()).divide(currentPrice);
        BigDecimal number3 = endFuture.getMarginBalance().subtract(endDebitFuture).subtract(futureFirstBalance.getCurrentAmount());*/

        return null;
    }

    /**
     * 获得本次运行盈亏
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getCurrProfit() {
        FutureBalanceReqDto balanceReqDto = new FutureBalanceReqDto();
        balanceReqDto.setAccountId(futureAccountId);
        balanceReqDto.setCoinType(futureCoinType);
        balanceReqDto.setExchangeId(futureExchangeId);
        ServiceResult<FutureBalanceRespDto> start = futureAccountService.getAccountInfo(futureAccountId, futureContractCode);
        ServiceResult<FutureBalanceRespDto> end = futureAccountService.getBalance(balanceReqDto);
        if (start.getCode() != ServiceErrorEnum.SUCCESS.getCode() || end.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
            String msg = null;
            if (start.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
                msg = start.getMessage();
            } else {
                msg = end.getMessage();
            }
            logger.error("取不到期货用户余额，错误信息：{}", msg);
        }
        FutureBalance startFuture = new FutureBalance();
        FutureBalance endFuture = new FutureBalance();
        startFuture.setMarginBalance(start.getData().getData().get(futureCoinType).getMarginBalance());
        endFuture.setMarginBalance(end.getData().getData().get(futureCoinType).getMarginBalance());
        SpotBalanceReqDto balanceReqDto2 = new SpotBalanceReqDto();
        balanceReqDto2.setAccountId(spotAccountId);
        balanceReqDto2.setExchangeId(spotExchangeId);
        ServiceResult<SpotBalanceRespDto> end2 = spotAccountService.getBalance(balanceReqDto2);
        if (end2.getCode() != ServiceErrorEnum.SUCCESS.getCode()) {
            logger.error("取不到现货用户当前余额，错误信息：{}", end2.getMessage());
        }
        List<QuanAccountAsset> firstBalance = spotAccountService.getFirstBalance(spotAccountId, spotExchangeId);
        SpotBalance startSpotCoin1 = new SpotBalance();
        SpotBalance startSpotCoin2 = new SpotBalance();
        SpotBalance endSpotCoin1 = new SpotBalance();
        SpotBalance endSpotCoin2 = new SpotBalance();
        for (QuanAccountAsset quanAccountAsset : firstBalance) {
            if (spotBaseCoin.equals(quanAccountAsset.getCoin())) {
                startSpotCoin1.setTotal(quanAccountAsset.getTotal());
            }
            if (spotQuoteCoin.equals(quanAccountAsset.getCoin())) {
                startSpotCoin2.setTotal(quanAccountAsset.getTotal());
            }
        }
        endSpotCoin1.setTotal(end2.getData().getData().get(spotBaseCoin).getTotal());
        endSpotCoin2.setTotal(end2.getData().getData().get(spotQuoteCoin).getTotal());
        //获取币币账户的两个币种的期末净借贷和合约用户的期末净借贷
       /* BigDecimal endDebitCoin1 = getEndDebit(spotBaseCoin, spotExchangeId, spotAccountId);
        BigDecimal endDebitcoin2 = getEndDebit(spotQuoteCoin, spotExchangeId, spotAccountId);
        BigDecimal endDebitFuture = getEndDebit(futureCoinType, futureExchangeId, futureAccountId);*/
        //获取币币账户的两个币种的期初净借贷和合约用户的期初净借贷
        Map<String, BigDecimal> startDebit = getStartDebit(futureContractCode);
        BigDecimal startDebitCoin1 = startDebit.get(spotBaseCoin + "_" + spotExchangeId + "_" + spotAccountId);
        BigDecimal startDebitCoin2 = startDebit.get(spotQuoteCoin + "_" + spotExchangeId + "_" + spotAccountId);
        BigDecimal startDebitFuture = startDebit.get(futureCoinType + "_" + futureExchangeId + "_" + futureAccountId);
        //获取币币交易最新成交价
        SpotCurrentPriceReqDto currentPriceReqDto = new SpotCurrentPriceReqDto();
        currentPriceReqDto.setBaseCoin(spotBaseCoin);
        currentPriceReqDto.setQuoteCoin(spotQuoteCoin);
        currentPriceReqDto.setExchangeId(spotExchangeId);
        ServiceResult<SpotCurrentPriceRespDto> currentPriceResp = spotMarketService.getCurrentPrice(currentPriceReqDto);
        BigDecimal currentPrice = currentPriceResp.getData().getCurrentPrice();
      /*  BigDecimal number1 = endSpotCoin1.getTotal().subtract(endDebitCoin1).
                subtract(startSpotCoin1.getTotal().subtract(startDebitCoin1));
        BigDecimal number2 = endSpotCoin2.getTotal().subtract(endDebitcoin2).
                subtract(startSpotCoin2.getTotal().subtract(startDebitCoin2)).divide(currentPrice);
        BigDecimal number3 = endFuture.getMarginBalance().subtract(endDebitFuture).
                subtract(startFuture.getMarginBalance().subtract(startDebitFuture));*/
        return null;
    }

    /**
     * 获取期初的净借贷
     */
    private Map<String, BigDecimal> getStartDebit(String contractCode) {
        Map<String, BigDecimal> firstDebit = spotAccountService.getFirstDebit(contractCode);
        return firstDebit;
    }


    /**
     * 调用接口方法获取当前账号组净头寸，并返回
     *
     * @param contractCode
     * @return
     */
    public BigDecimal getNetPosition() {
        StartHedgingParam startHedgingParam = new StartHedgingParam();
        startHedgingParam.setBaseCoin(spotBaseCoin);
        startHedgingParam.setQuoteCoin(spotQuoteCoin);
        startHedgingParam.setFutureAccountID(futureAccountId);
        startHedgingParam.setContractCode(futureContractCode);
        startHedgingParam.setFutureExchangeId(futureExchangeId);
        startHedgingParam.setSpotAccountID(spotAccountId);
        startHedgingParam.setSpotExchangeId(spotExchangeId);
        BigDecimal calUSDTPosition = commonService.calUSDTPosition(startHedgingParam);
        return calUSDTPosition;
    }


    public StrategyRiskConfig getStrategyRiskConfig() {
        return strategyRiskMapper.selectByPrimaryKey(1);
    }

    /**
     * @param tradeCtrl 0-正常，1-停止下开仓单，只下平仓单，2-停止合约摆盘，撤销两账户所有未成交订单
     * @param hedgeCtrl 0-正常，1- 停止对冲程序，撤销两账户所有未成交订单
     */
    public void updateRiskCtrl(Integer tradeCtrl, Integer hedgeCtrl) {
        StrategyRiskConfig riskConfig = new StrategyRiskConfig();
        riskConfig.setSymbol(this.futureSymbol);
        riskConfig.setContractType(this.futureContractType);
        riskConfig.setTradeCtrl(tradeCtrl);
        riskConfig.setHedgeCtrl(hedgeCtrl);
        strategyRiskMapper.updateBySymbolTypeSelective(riskConfig);
    }
}
