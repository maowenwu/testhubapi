package com.huobi.quantification.service.job.impl;

import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.job.okcoin.*;
import com.huobi.quantification.quartz.QuartzManager;
import com.huobi.quantification.service.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private QuanJobFutureMapper quanJobFutureMapper;

    @Override
    public void updateFutureJobScheduler() {
        List<QuanJobFuture> jobFutures = quanJobFutureMapper.selectAll();
        for (QuanJobFuture jobFuture : jobFutures) {
            if (jobFuture.getState().equals(1)) {
                Class jobClass = getJobClass(jobFuture.getExchangeId(), jobFuture.getJobType());
                if (jobClass == null) {
                    continue;
                }
                quartzManager.addJobNoRepeat(jobFuture.getJobName(), jobClass, jobFuture.getCron(), jobFuture);
            } else {
                quartzManager.removeJobNoRepeat(jobFuture.getJobName());
            }
        }
    }

    private Class getJobClass(int exchangeId, int jobType) {
        Class clazz = null;
        if (exchangeId == ExchangeEnum.OKEX.getExId()) {
            switch (jobType) {
                case 1:
                    clazz = OkFutureDepthJob.class;
                    break;
                case 2:
                    clazz = OkFutureKlineJob.class;
                    break;
                case 3:
                    clazz = OkFutureTickerJob.class;
                    break;
                case 4:
                    clazz = OkFutureUserInfoJob.class;
                    break;
                case 5:
                    clazz = OkFuturePositionJob.class;
                    break;
                case 6:
                    clazz = OkFutureOrderJob.class;
                    break;
                default:
                    clazz = null;
            }
        }
        return clazz;
    }

}
