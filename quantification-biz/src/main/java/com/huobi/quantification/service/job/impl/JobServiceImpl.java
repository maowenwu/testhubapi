package com.huobi.quantification.service.job.impl;

import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dao.QuanJobMapper;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.job.huobi.HuobiDepthJob;
import com.huobi.quantification.job.huobi.HuobiOrderJob;
import com.huobi.quantification.job.huobi.HuobiTickerJob;
import com.huobi.quantification.job.huobi.HuobiAccountJob;
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
    
    @Autowired
    private QuanJobMapper quanJobMapper;

    @Override
    public void updateFutureJobScheduler() {
        List<QuanJobFuture> jobFutures = quanJobFutureMapper.selectAll();
        for (QuanJobFuture jobFuture : jobFutures) {
            if (jobFuture.getState().equals(1)) {
                Class jobClass = getFutureJobClass(jobFuture.getExchangeId(), jobFuture.getJobType());
                if (jobClass == null) {
                    continue;
                }
                quartzManager.addJobNoRepeat(jobFuture.getJobName(), jobClass, jobFuture.getCron(), jobFuture);
            } else {
                quartzManager.removeJobNoRepeat(jobFuture.getJobName());
            }
        }
    }

    private Class getFutureJobClass(int exchangeId, int jobType) {
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
			}else {
				quartzManager.removeJobNoRepeat(quanJob.getJobName());
			}
		}
	}

	private Class getJobClass(int exchangeId, int jobType) {
        Class clazz = null;
        if (exchangeId == ExchangeEnum.HUOBI.getExId()) {
            switch (jobType) {
                case 1:
                    clazz = HuobiDepthJob.class;
                    break;
                case 2:
                    clazz = HuobiTickerJob.class;
                    break;
                case 3:
                	clazz = HuobiAccountJob.class;
                	break;
                case 4:
                	clazz = HuobiOrderJob.class;
                	break;
                default:
                    clazz = null;
            }
        }
        return clazz;
    }
}
