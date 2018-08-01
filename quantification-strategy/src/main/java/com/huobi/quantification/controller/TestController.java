package com.huobi.quantification.controller;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.strategy.config.StrategyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {


    @Autowired
    private FutureMarketService futureMarketService;

    @Autowired
    private FutureAccountService futureAccountService;

    @Autowired
    private JobManageService jobManageService;

    @Autowired
    private FutureOrderService futureOrderService;

    @Autowired
    private StrategyProperties strategyProperties;

    @RequestMapping("/testProperties")
    public String testProperties() {

        System.out.println(strategyProperties);
        return JSON.toJSONString("ok");
    }


    @RequestMapping("/cancelOrder")
    public String cancelOrder() {
        FutureCancelOrderReqDto orderReqDto = new FutureCancelOrderReqDto();
        orderReqDto.setExchangeId(2);
        orderReqDto.setAccountId(1L);

        List<FutureCancelOrder> orderList = new ArrayList<>();
        FutureCancelOrder order = new FutureCancelOrder();
        order.setInnerOrderId(21L);
        orderList.add(order);

        orderReqDto.setOrders(orderList);
        orderReqDto.setTimeInterval(100);
        orderReqDto.setParallel(false);
        orderReqDto.setSync(true);
        ServiceResult serviceResult = futureOrderService.cancelOrder(orderReqDto);
        return JSON.toJSONString(serviceResult);
    }

    @RequestMapping("/placeOrder")
    public String placeOrder() {
        FuturePlaceOrderReqDto orderReqDto = new FuturePlaceOrderReqDto();
        orderReqDto.setExchangeId(2);
        orderReqDto.setAccountId(1L);
        orderReqDto.setBaseCoin("btc");
        orderReqDto.setQuoteCoin("usd");
        orderReqDto.setContractType("this_week");
        orderReqDto.setSide(1);
        orderReqDto.setOffset(1);
        orderReqDto.setPrice(BigDecimal.valueOf(7800));
        orderReqDto.setQuantity(BigDecimal.ONE);
        orderReqDto.setLever(10);
        orderReqDto.setSync(true);
        ServiceResult<FuturePlaceOrderRespDto> serviceResult = futureOrderService.placeOrder(orderReqDto);
        return JSON.toJSONString(serviceResult);
    }

    @RequestMapping("/testStartFutureJob")
    public String testStartFutureJob() {
       /* jobManageService.addOkFutureCurrentPriceJob("bch_usd", "next_week", "0/1 * * * * ?", false);
        jobManageService.addOkFutureDepthJob("bch_usd", "next_week", "0/1 * * * * ?", false);
        jobManageService.addOkFutureIndexJob("bch_usd", "0/1 * * * * ?", false);
        jobManageService.addOkFutureKlineJob("bch_usd", "5min", "next_week", "0/1 * * * * ?", false);
        jobManageService.addOkFutureOrderJob(1L, "bch_usd", "next_week", "0/1 * * * * ?", false);
        jobManageService.addOkFuturePositionJob(1L, "0/1 * * * * ?", false);
        jobManageService.addOkFutureUserInfoJob(1L, "0/1 * * * * ?", false);*/
        jobManageService.addOkFutureOrderJob(1L, "btc_usd", "this_week", "0/1 * * * * ?", true);
        return JSON.toJSONString(true);
    }

    @RequestMapping("/testFutureIndex")
    public String testFutureIndex() {
        FutureCurrentIndexReqDto reqDto = new FutureCurrentIndexReqDto();
        reqDto.setExchangeId(2);
        reqDto.setBaseCoin("btc");
        reqDto.setQuoteCoin("usd");
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(1);
        ServiceResult<FutureCurrentIndexRespDto> currentIndexPrice = futureMarketService.getCurrentIndexPrice(reqDto);
        System.out.println(currentIndexPrice);
        return JSON.toJSONString(currentIndexPrice);
    }

    @RequestMapping("/testFutureKline")
    public String testFutureKline() {
        FutureKlineReqDto reqDto = new FutureKlineReqDto();
        reqDto.setExchangeId(2);
        reqDto.setBaseCoin("btc");
        reqDto.setQuoteCoin("usd");

        reqDto.setPeriod("1min");
        reqDto.setContractType("this_week");

        reqDto.setTimeout(100);
        reqDto.setMaxDelay(60);
        ServiceResult<FutureKlineRespDto> kline = futureMarketService.getKline(reqDto);
        System.out.println(kline);
        return JSON.toJSONString(kline);
    }

    @RequestMapping("/testFutureDepth")
    public String testFutureDepth() {
        FutureDepthReqDto reqDto = new FutureDepthReqDto();
        reqDto.setExchangeId(2);
        reqDto.setBaseCoin("btc");
        reqDto.setQuoteCoin("usd");

        reqDto.setContractType("this_week");

        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3);

        ServiceResult<FutureDepthRespDto> depth = futureMarketService.getDepth(reqDto);
        System.out.println(depth);
        return JSON.toJSONString(depth);
    }


    @RequestMapping("/testFutureCurrentPrice")
    public String testFutureCurrentPrice() {
        FutureCurrentPriceReqDto reqDto = new FutureCurrentPriceReqDto();
        reqDto.setExchangeId(2);
        reqDto.setBaseCoin("btc");
        reqDto.setQuoteCoin("usd");

        reqDto.setContractType("this_week");

        reqDto.setTimeout(100);
        reqDto.setMaxDelay(30);

        ServiceResult<FutureCurrentPriceRespDto> currentPrice = futureMarketService.getCurrentPrice(reqDto);
        System.out.println(currentPrice);
        return JSON.toJSONString(currentPrice);
    }


    @RequestMapping("/testGetBalance")
    public String testGetBalance() {
        FutureBalanceReqDto reqDto = new FutureBalanceReqDto();
        reqDto.setExchangeId(0);
        reqDto.setAccountId(101L);

        reqDto.setCoinType("btc");
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3000);

        ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(reqDto);
        System.out.println(balance);
        return JSON.toJSONString(balance);
    }


    @RequestMapping("/testGetPosition")
    public String testGetPosition() {
        FuturePositionReqDto reqDto = new FuturePositionReqDto();
        reqDto.setExchangeId(0);
        reqDto.setAccountId(101L);

        reqDto.setCoinType("btc");
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3);

        ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(reqDto);
        System.out.println(position);
        return JSON.toJSONString(position);
    }
}
