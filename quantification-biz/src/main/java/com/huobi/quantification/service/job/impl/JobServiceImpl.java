package com.huobi.quantification.service.job.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dao.QuanJobMapper;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.JobTypeEnum;
import com.huobi.quantification.job.huobi.future.*;
import com.huobi.quantification.job.huobi.spot.HuobiDepthJob;
import com.huobi.quantification.job.huobi.spot.HuobiKlineJob;
import com.huobi.quantification.job.huobi.spot.HuobiAccountJob;
import com.huobi.quantification.job.huobi.spot.HuobiCurrentPriceJob;
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
        okFutureTypeClass.put(JobTypeEnum.Depth.getJobType(), OkFutureDepthJob.class);
        okFutureTypeClass.put(JobTypeEnum.Kline.getJobType(), OkFutureKlineJob.class);
        okFutureTypeClass.put(JobTypeEnum.Account.getJobType(), OkFutureUserInfoJob.class);
        okFutureTypeClass.put(JobTypeEnum.Position.getJobType(), OkFuturePositionJob.class);
        okFutureTypeClass.put(JobTypeEnum.Order.getJobType(), OkFutureOrderJob.class);
        okFutureTypeClass.put(JobTypeEnum.Index.getJobType(), OkFutureIndexJob.class);
        okFutureTypeClass.put(JobTypeEnum.CurrentPrice.getJobType(), OkFutureCurrentPriceJob.class);
        okFutureTypeClass.put(JobTypeEnum.ContractCode.getJobType(), OkFutureContractCodeJob.class);
        jobMap.put(ExchangeEnum.OKEX.getExId(), okFutureTypeClass);

        Map<Integer, Class> huobiFutureTypeClass = new HashMap<>();
        huobiFutureTypeClass.put(JobTypeEnum.Depth.getJobType(), HuobiFutureDepthJob.class);
        huobiFutureTypeClass.put(JobTypeEnum.Kline.getJobType(), HuobiFutureKlineJob.class);
        huobiFutureTypeClass.put(JobTypeEnum.Account.getJobType(),HuobiFutureAccountJob.class);
        huobiFutureTypeClass.put(JobTypeEnum.Order.getJobType(),HuobiFutureOrderJob .class);
        huobiFutureTypeClass.put(JobTypeEnum.CurrentPrice.getJobType(),HuobiFutureCurrentPriceJob .class);
        huobiFutureTypeClass.put(JobTypeEnum.Position.getJobType(),HuobiFuturePositionJob .class);
        huobiFutureTypeClass.put(JobTypeEnum.Index.getJobType(),HuobiFutureIndexJob .class);
        huobiFutureTypeClass.put(JobTypeEnum.ContractCode.getJobType(),HuobiFutureContractCodeJob .class);
        jobMap.put(ExchangeEnum.HUOBI_FUTURE.getExId(), huobiFutureTypeClass);


        Map<Integer, Class> huobiSpotTypeClass = new HashMap<>();
        huobiSpotTypeClass.put(JobTypeEnum.Depth.getJobType(), HuobiDepthJob.class);
        huobiSpotTypeClass.put(JobTypeEnum.Kline.getJobType(), HuobiKlineJob.class);
        huobiSpotTypeClass.put(JobTypeEnum.Account.getJobType(), HuobiAccountJob.class);
        huobiSpotTypeClass.put(JobTypeEnum.CurrentPrice.getJobType(), HuobiCurrentPriceJob.class);
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
                JobParamDto jobParamDto = JSON.parseObject(quanJob.getJobParam(), JobParamDto.class);
                quartzManager.addJobNoRepeat(quanJob.getJobName(), jobClass, quanJob.getCron(), jobParamDto);
            } else {
                quartzManager.removeJobNoRepeat(quanJob.getJobName());
            }
        }
    }

    private Class getJobClass(int exchangeId, int jobType) {
        return jobMap.get(exchangeId).get(jobType);
    }
}
