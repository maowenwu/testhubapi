package com.huobi.quantification.service.job.impl;

import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.job.okcoin.OkFutureDepthJob;
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
                quartzManager.addJobNoRepeat(jobFuture.getJobName(), OkFutureDepthJob.class, jobFuture.getCron(), jobFuture);
            } else {
                quartzManager.removeJobNoRepeat(jobFuture.getJobName());
            }
        }
    }
}
