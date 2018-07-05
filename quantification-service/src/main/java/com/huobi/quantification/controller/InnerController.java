package com.huobi.quantification.controller;


import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.okcoin.OkFutureDepthJob;
import com.huobi.quantification.quartz.QuartzManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InnerController {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private QuanJobFutureMapper quanJobFutureMapper;

 /*   @Autowired
    private OkSecretHolder okSecretHolder;*/

    @RequestMapping("/test")
    public String test() {
        QuanJobFuture quanJobFuture = quanJobFutureMapper.selectByPrimaryKey(1);
        quartzManager.addJobNoRepeat(quanJobFuture.getJobName(), OkFutureDepthJob.class, quanJobFuture.getCron(), quanJobFuture);
        System.out.println(quartzManager);
        return "ok";
    }
}
