package com.huobi.quantification.strategy.risk;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.strategy.config.StrategyProperties;
import com.huobi.quantification.strategy.risk.enums.RechargeTypeEnum;

@Component
public class RiskBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StrategyProperties strategyProperties;
    @Autowired
    private FutureAccountService futureAccountService;
    @Autowired
    private SpotAccountService spotAccountService;
    @Autowired
    private QuanAccountHistoryMapper quanAccountHistoryMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            logger.info("==>spring 容器启动");
            StrategyProperties.ConfigGroup group1 = strategyProperties.getGroup1();
            if (group1.getEnable()) {
                startWithConfig(group1);
            }
            StrategyProperties.ConfigGroup group2 = strategyProperties.getGroup2();
            if (group2.getEnable()) {
                startWithConfig(group2);
            }
            StrategyProperties.ConfigGroup group3 = strategyProperties.getGroup3();
            if (group3.getEnable()) {
                startWithConfig(group3);
            }
        }
    }

    private void startWithConfig(StrategyProperties.ConfigGroup group) {
        StrategyProperties.Config future = group.getFuture();
        StrategyProperties.Config spot = group.getSpot();
        // 项目启动时，获取用户的余额，持仓和净借贷
        Long spotAccountId = spot.getAccountId();
        Integer spotExchangeId = spot.getExchangeId();
        String baseCoin = spot.getBaseCoin();
        String quotCoin = spot.getQuotCoin();

        Long futureAccountId = future.getAccountId();
        Integer futureExchangeId = future.getExchangeId();
        String baseCoin2 = future.getBaseCoin();
        String contractCode = future.getContractCode();

        spotAccountService.saveFirstBalance(spotAccountId, spotExchangeId);
        futureAccountService.saveAccountsInfo(futureAccountId, contractCode);

        BigDecimal spotDebitCoin1 = getEndDebit(baseCoin, spotExchangeId, spotAccountId);
        BigDecimal spotDebitCoin2 = getEndDebit(quotCoin, spotExchangeId, spotAccountId);
        BigDecimal futureDebit = getEndDebit(baseCoin2, futureExchangeId, futureAccountId);

        HashMap<String, BigDecimal> hashMap = new HashMap<>();
        hashMap.put(baseCoin + "_" + spotExchangeId + "_" + spotAccountId, spotDebitCoin1);
        hashMap.put(quotCoin + "_" + spotExchangeId + "_" + spotAccountId, spotDebitCoin2);
        hashMap.put(baseCoin2 + "_" + futureExchangeId + "_" + futureAccountId, futureDebit);
        spotAccountService.saveFirstDebit(hashMap, future.getContractCode());

        RiskManager riskManager = ApplicationContextHolder.getContext().getBean(RiskManager.class);
        riskManager.init(group);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    riskManager.monitor();
                } catch (Throwable e) {
                    logger.error("监控保证金率期间出现异常", e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private BigDecimal getEndDebit(String coin, int exchangeId, long accountId) {
        BigDecimal borrow = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin,
                RechargeTypeEnum.BORROW.getRechargeType());
        BigDecimal payment = quanAccountHistoryMapper.getAomuntByRechargeType(accountId, exchangeId, coin,
                RechargeTypeEnum.REPAYMENT.getRechargeType());
        return borrow.subtract(payment);
    }

}
