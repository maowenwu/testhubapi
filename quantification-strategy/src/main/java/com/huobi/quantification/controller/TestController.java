package com.huobi.quantification.controller;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OkJobTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    private FutureMarketService futureMarketService;

    @Autowired
    private FutureAccountService futureAccountService;

    @Autowired
    private JobManageService jobManageService;

    @RequestMapping("/testStartFutureJob")
    public String testStartFutureJob() {
        jobManageService.addOkFutureCurrentPriceJob("btc_usd", "this_week", "0/1 * * * * ?", true);
        jobManageService.addOkFutureDepthJob("btc_usd", "this_week", "0/1 * * * * ?", true);
        jobManageService.addOkFutureIndexJob("btc_usd", "0/1 * * * * ?", true);
        jobManageService.addOkFutureKlineJob("btc_usd", "5min", "this_week", "0/1 * * * * ?", true);
        jobManageService.addOkFutureOrderJob(1L, "btc_usd", "this_week", "0/1 * * * * ?", true);
        jobManageService.addOkFuturePositionJob(1L, "0/1 * * * * ?", true);
        jobManageService.addOkFutureUserInfoJob(1L, "0/1 * * * * ?", true);
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
        reqDto.setExchangeId(2);
        reqDto.setAccountId(1L);

        reqDto.setCoinType("btc");
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3);

        ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(reqDto);
        System.out.println(balance);
        return JSON.toJSONString(balance);
    }


    @RequestMapping("/testGetPosition")
    public String testGetPosition() {
        FuturePositionReqDto reqDto = new FuturePositionReqDto();
        reqDto.setExchangeId(2);
        reqDto.setAccountId(1L);

        reqDto.setCoinType("ltc");
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3);

        ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(reqDto);
        System.out.println(position);
        return JSON.toJSONString(position);
    }
}
