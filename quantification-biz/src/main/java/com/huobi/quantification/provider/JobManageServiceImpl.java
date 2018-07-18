package com.huobi.quantification.provider;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dto.FutureJobReqDto;
import com.huobi.quantification.dto.JobParamDto;
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
    public ServiceResult startFutureJob(FutureJobReqDto jobReqDto) {
        ServiceResult result = new ServiceResult();
        try {
            updateJobFuture(jobReqDto.getExchangeId(), jobReqDto.getJobType(), jobReqDto.getJobParamDto(), jobReqDto.getCron(), 1);
        } catch (Exception e) {
            logger.error("启动任务异常exchangeId={}，jobType={}", jobReqDto.getExchangeId(), jobReqDto.getJobType(), e);
            result.setCode(ServiceErrorEnum.JOB_START_ERROR.getCode());
            result.setMessage(ServiceErrorEnum.JOB_START_ERROR.getMessage());
            return result;
        }
        result.setCode(ServiceErrorEnum.SUCCESS.getCode());
        result.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        return result;
    }

    @Override
    public ServiceResult stopFutureJob(FutureJobReqDto jobReqDto) {
        ServiceResult result = new ServiceResult();
        try {
            updateJobFuture(jobReqDto.getExchangeId(), jobReqDto.getJobType(), jobReqDto.getJobParamDto(), jobReqDto.getCron(), 0);
        } catch (Exception e) {
            logger.error("停止任务异常exchangeId={}，jobType={}", jobReqDto.getExchangeId(), jobReqDto.getJobType(), e);
            result.setCode(ServiceErrorEnum.JOB_STOP_ERROR.getCode());
            result.setMessage(ServiceErrorEnum.JOB_STOP_ERROR.getMessage());
            return result;
        }
        result.setCode(ServiceErrorEnum.SUCCESS.getCode());
        result.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        return result;
    }

    private void updateJobFuture(int exchangeId, int jobType, JobParamDto jobParamDto, String cron, int state) {
        QuanJobFuture jobFuture = new QuanJobFuture();
        jobFuture.setExchangeId(exchangeId);
        jobFuture.setJobType(jobType);
        // JobName需要唯一
        jobFuture.setJobName(genJobName(exchangeId, jobType, jobParamDto));
        jobFuture.setJobParam(JSON.toJSONString(jobParamDto));
        jobFuture.setCron(cron);
        jobFuture.setState(state);
        jobFuture.setCreateDate(new Date());
        jobFuture.setUpdateDate(new Date());
        quanJobFutureMapper.insertOrUpdate(jobFuture);
    }

    private String genJobName(int exchangeId, int jobType, JobParamDto jobParamDto) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(exchangeId).append("_")
                .append(jobType).append("_");
        if (jobParamDto != null) {
            buffer.append(JSON.toJSON(jobParamDto));
        }
        return buffer.toString();
    }
}
