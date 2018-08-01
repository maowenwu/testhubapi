package com.huobi.quantification.controller;


import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {


    @Autowired
    private ConfigService configService;

    @RequestMapping("/test")
    public String test() {
        StrategyOrderConfig strategyOrderConfig = configService.selectByPrimaryKey(1);
        System.out.println(strategyOrderConfig);
        return "index";
    }
}
