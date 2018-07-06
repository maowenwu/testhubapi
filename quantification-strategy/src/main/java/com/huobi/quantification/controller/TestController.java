package com.huobi.quantification.controller;


import com.huobi.quantification.api.JobManageService;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.OkJobTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JobManageService jobManageService;

    @RequestMapping("/test")
    public String test() {

        jobManageService.stopFutureJob(ExchangeEnum.OKEX.getExId(),OkJobTypeEnum.Depth.getJobType(),null,"btc_usd","this_week","0/1 * * * * ?");

        System.out.println(jobManageService);
        return "ok";
    }
}
