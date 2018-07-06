package com.huobi.quantification.provider;

import com.huobi.quantification.api.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ServiceErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("jobManageService")
public class JobManageServiceImpl implements JobManageService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuanJobFutureMapper quanJobFutureMapper;

    @Override
    public ServiceResult startFutureJob(int exchangeId, int jobType, Long accountId, String symbol, String contractType, String cron) {
        ServiceResult result = new ServiceResult();
        try {
            updateJobFuture(exchangeId, jobType, accountId, symbol, contractType, cron, 1);
        } catch (Exception e) {
            logger.error("启动任务异常exchangeId={}，jobType={}", exchangeId, jobType, e);
            result.setCode(ServiceErrorEnum.JOB_START_ERROR.getCode());
            result.setMessage(ServiceErrorEnum.JOB_START_ERROR.getMessage());
            return result;
        }
        result.setCode(ServiceErrorEnum.SUCCESS.getCode());
        result.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        return result;
    }

    @Override
    public ServiceResult stopFutureJob(int exchangeId, int jobType, Long accountId, String symbol, String contractType, String cron) {
        ServiceResult result = new ServiceResult();
        try {
            updateJobFuture(exchangeId, jobType, accountId, symbol, contractType, cron, 0);
        } catch (Exception e) {
            logger.error("停止任务异常exchangeId={}，jobType={}", exchangeId, jobType, e);
            result.setCode(ServiceErrorEnum.JOB_STOP_ERROR.getCode());
            result.setMessage(ServiceErrorEnum.JOB_STOP_ERROR.getMessage());
            return result;
        }
        result.setCode(ServiceErrorEnum.SUCCESS.getCode());
        result.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        return result;
    }

    private void updateJobFuture(int exchangeId, int jobType, Long accountId, String symbol, String contractType, String cron, int state) {
        QuanJobFuture jobFuture = new QuanJobFuture();
        jobFuture.setExchangeId(exchangeId);
        jobFuture.setJobType(jobType);
        jobFuture.setJobName(genJobName(exchangeId, jobType, accountId, symbol, contractType));
        jobFuture.setAccountId(accountId);
        jobFuture.setSymbol(symbol);
        jobFuture.setContractType(contractType);
        jobFuture.setCron(cron);
        jobFuture.setState(state);
        jobFuture.setCreateDate(new Date());
        jobFuture.setUpdateDate(new Date());
        quanJobFutureMapper.insertOrUpdate(jobFuture);
    }

    private String genJobName(int exchangeId, int jobType, Long accountId, String symbol, String contractType) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(exchangeId).append("_")
                .append(jobType).append("_");
        if (accountId != null) {
            buffer.append(accountId).append("_");
        }
        if (symbol != null) {
            buffer.append(symbol).append("_");
        }
        if (contractType != null) {
            buffer.append(contractType).append("_");
        }
        return buffer.toString();
    }
}
