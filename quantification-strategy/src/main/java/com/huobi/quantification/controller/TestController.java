package com.huobi.quantification.controller;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.api.future.FutureOrderService;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.enums.ExchangeEnum;
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
    private SpotAccountService spotAccountService;

    @RequestMapping("/getBalance")
    public String getBalance() {
        SpotBalanceReqDto reqDto = new SpotBalanceReqDto();
        reqDto.setExchangeId(1);
        reqDto.setAccountId(4232061);
        ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(reqDto);
        return JSON.toJSONString(balance);
    }

    @RequestMapping("/addHuobiSpotAccountJob")
    public String addHuobiSpotAccountJob() {
        jobManageService.addHuobiSpotAccountJob(4232061L, "0/1 * * * * ?", true);

        return JSON.toJSONString("ok");
    }

    @RequestMapping("/testProperties")
    public String testProperties() {


        return JSON.toJSONString("ok");
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


    @RequestMapping("/getDepth")
    public String getDepth(){
        FutureDepthReqDto reqDto = new FutureDepthReqDto();
        reqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        reqDto.setBaseCoin("btc");
        reqDto.setQuoteCoin("usdt");
        reqDto.setContractType("this_week");
        ServiceResult<FutureDepthRespDto> depth = futureMarketService.getDepth(reqDto);
        return JSON.toJSONString(depth);
    }
}
