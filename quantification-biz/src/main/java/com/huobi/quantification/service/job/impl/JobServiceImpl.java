package com.huobi.quantification.service.job.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dao.QuanJobMapper;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.job.huobi.future.*;
import com.huobi.quantification.job.huobi.spot.HuobiDepthJob;
import com.huobi.quantification.job.huobi.spot.HuobiOrderJob;
import com.huobi.quantification.job.huobi.spot.HuobiTickerJob;
import com.huobi.quantification.job.huobi.spot.HuobiAccountJob;
import com.huobi.quantification.job.okcoin.future.*;
import com.huobi.quantification.quartz.QuartzManager;
import com.huobi.quantification.service.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private QuanJobFutureMapper quanJobFutureMapper;

    @Autowired
    private QuanJobMapper quanJobMapper;

    private static Map<Integer, Map<Integer, Class>> jobMap = new HashMap<>();

    static {
        Map<Integer, Class> okFutureTypeClass = new HashMap<>();
        okFutureTypeClass.put(1, OkFutureDepthJob.class);
        okFutureTypeClass.put(2, OkFutureKlineJob.class);
        okFutureTypeClass.put(3, OkFutureTickerJob.class);
        okFutureTypeClass.put(4, OkFutureUserInfoJob.class);
        okFutureTypeClass.put(5, OkFuturePositionJob.class);
        okFutureTypeClass.put(6, OkFutureOrderJob.class);
        okFutureTypeClass.put(7, OkFutureIndexJob.class);
        okFutureTypeClass.put(8, OkFutureCurrentPriceJob.class);
        jobMap.put(ExchangeEnum.OKEX.getExId(), okFutureTypeClass);

        Map<Integer, Class> huobiFutureTypeClass = new HashMap<>();
        huobiFutureTypeClass.put(1, HuobiFutureDepthJob.class);
        huobiFutureTypeClass.put(2, HuobiFutureKlineJob.class);
        huobiFutureTypeClass.put(4, HuobiFutureUserInfoJob.class);
        huobiFutureTypeClass.put(5, HuobiFuturePositionJob.class);
        huobiFutureTypeClass.put(7, HuobiFutureIndexJob.class);
        huobiFutureTypeClass.put(8, HuobiFutureCurrentPriceJob.class);
        jobMap.put(ExchangeEnum.HUOBI_FUTURE.getExId(), huobiFutureTypeClass);


        Map<Integer, Class> huobiSpotTypeClass = new HashMap<>();
        huobiSpotTypeClass.put(1, HuobiDepthJob.class);
        huobiSpotTypeClass.put(2, HuobiTickerJob.class);
        huobiSpotTypeClass.put(3, HuobiAccountJob.class);
        huobiSpotTypeClass.put(4, HuobiOrderJob.class);
        jobMap.put(ExchangeEnum.HUOBI.getExId(), huobiSpotTypeClass);
    }

    @Override
    public void updateFutureJobScheduler() {
        List<QuanJobFuture> jobFutures = quanJobFutureMapper.selectAll();
        for (QuanJobFuture jobFuture : jobFutures) {
            if (jobFuture.getState().equals(1)) {
                Class jobClass = getJobClass(jobFuture.getExchangeId(), jobFuture.getJobType());
                if (jobClass == null) {
                    continue;
                }
                JobParamDto jobParamDto = JSON.parseObject(jobFuture.getJobParam(), JobParamDto.class);
                quartzManager.addJobNoRepeat(jobFuture.getJobName(), jobClass, jobFuture.getCron(), jobParamDto);
            } else {
                quartzManager.removeJobNoRepeat(jobFuture.getJobName());
            }
        }
    }

    @Override
    public void updateJobScheduler() {
        List<QuanJob> quanJobs = quanJobMapper.selectAll();
        for (QuanJob quanJob : quanJobs) {
            if (quanJob.getState().equals(1)) {
                Class jobClass = getJobClass(quanJob.getExchangeId(), quanJob.getJobType());
                if (jobClass == null) {
                    continue;
                }
                quartzManager.addJobNoRepeat(quanJob.getJobName(), jobClass, quanJob.getCron(), quanJob);
            } else {
                quartzManager.removeJobNoRepeat(quanJob.getJobName());
            }
        }
    }

    private Class getJobClass(int exchangeId, int jobType) {
        return jobMap.get(exchangeId).get(jobType);
    }
}
