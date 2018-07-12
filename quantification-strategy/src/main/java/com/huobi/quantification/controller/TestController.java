package com.huobi.quantification.controller;


import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureMarketService;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureCurrentIndexReqDto;
import com.huobi.quantification.dto.FutureCurrentIndexRespDto;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OkJobTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JobManageService jobManageService;

    @Autowired
    private FutureMarketService futureMarketService;

    @RequestMapping("/test")
    public String test() {

        //jobManageService.stopFutureJob(ExchangeEnum.OKEX.getExId(),OkJobTypeEnum.Depth.getJobType(),null,"btc_usd","this_week","0/1 * * * * ?");
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
}
