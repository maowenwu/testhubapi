package com.huobi.quantification.provider;

import java.util.Date;

import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.HuobiJobTypeEnum;
import com.huobi.quantification.enums.OkJobTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import com.huobi.quantification.dao.QuanJobMapper;
import com.huobi.quantification.dto.FutureJobReqDto;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.dto.JobReqDto;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ServiceErrorEnum;

@Service("jobManageService")
public class JobManageServiceImpl implements JobManageService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuanJobFutureMapper quanJobFutureMapper;
    @Autowired
    private QuanJobMapper quanJobMapper;


    public ServiceResult addFutureJob(FutureJobReqDto jobReqDto) {
        try {
            updateJobFuture(jobReqDto.getExchangeId(), jobReqDto.getJobType(),
                    jobReqDto.getJobParamDto(), jobReqDto.getJobDesc(), jobReqDto.getCron(), jobReqDto.getState());
        } catch (Exception e) {
            logger.error("启动任务异常exchangeId={}，jobType={}", jobReqDto.getExchangeId(), jobReqDto.getJobType(), e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.JOB_START_ERROR);
        }
        return ServiceResult.buildSuccessResult(null);
    }

    private void updateJobFuture(int exchangeId, int jobType, JobParamDto jobParamDto, String jobDesc, String cron, int state) {
        QuanJobFuture jobFuture = new QuanJobFuture();
        jobFuture.setExchangeId(exchangeId);
        jobFuture.setJobType(jobType);
        // JobName需要唯一
        jobFuture.setJobName(genJobName(exchangeId, jobType, jobParamDto));
        jobFuture.setJobParam(JSON.toJSONString(jobParamDto));
        jobFuture.setJobDesc(jobDesc);
        jobFuture.setCron(cron);
        jobFuture.setState(state);
        jobFuture.setCreateDate(new Date());
        jobFuture.setUpdateDate(new Date());
        quanJobFutureMapper.insertOrUpdate(jobFuture);
    }

    public ServiceResult addSpotJob(JobReqDto jobReqDto) {
        try {
            updateJob(jobReqDto.getExchangeId(), jobReqDto.getJobType(), jobReqDto.getJobDesc(), jobReqDto.getJobParamDto(),
                    jobReqDto.getCron(), jobReqDto.getState());
        } catch (Exception e) {
            logger.error("启动任务异常exchangeId={}，jobType={}", jobReqDto.getExchangeId(), jobReqDto.getJobType(), e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.JOB_START_ERROR);
        }
        return ServiceResult.buildSuccessResult(null);
    }

    private void updateJob(int exchangeId, int jobType, String jobDesc, JobParamDto jobParamDto, String cron, int state) {
        QuanJob quanJob = new QuanJob();
        quanJob.setExchangeId(exchangeId);
        quanJob.setJobType(jobType);
        // JobName需要唯一
        quanJob.setJobName(genJobName(exchangeId, jobType, jobParamDto));
        quanJob.setJobParam(JSON.toJSONString(jobParamDto));
        quanJob.setCron(cron);
        quanJob.setState(state);
        quanJob.setJobDesc(jobDesc);
        quanJob.setCreateDate(new Date());
        quanJob.setUpdateDate(new Date());
        quanJobMapper.insertOrUpdate(quanJob);
    }

    private String genJobName(int exchangeId, int jobType, JobParamDto jobParamDto) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(exchangeId).append("_").append(jobType).append("_");
        if (jobParamDto != null) {
            buffer.append(JSON.toJSON(jobParamDto));
        }
        return buffer.toString();
    }

    @Override
    public ServiceResult addOkFutureCurrentPriceJob(String symbol, String contractType, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.CurrentPrice.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.CurrentPrice.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setSymbol(symbol);
        paramDto.setContractType(contractType);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFutureDepthJob(String symbol, String contractType, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.Depth.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.Depth.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setSymbol(symbol);
        paramDto.setContractType(contractType);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFutureIndexJob(String symbol, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.Index.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.Index.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setSymbol(symbol);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFutureKlineJob(String symbol, String period, String contractType, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.Kline.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.Kline.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setSymbol(symbol);
        paramDto.setKlineType(period);
        paramDto.setContractType(contractType);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFutureOrderJob(Long accountId, String symbol, String contractType, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.Order.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.Order.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setAccountId(accountId);
        paramDto.setSymbol(symbol);
        paramDto.setContractType(contractType);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFuturePositionJob(Long accountId, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.Position.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.Position.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setAccountId(accountId);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addOkFutureUserInfoJob(Long accountId, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.UserInfo.getJobType());
        jobReqDto.setJobDesc(OkJobTypeEnum.UserInfo.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setAccountId(accountId);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiSpotAccountJob(Long accountId, String cron, boolean enable) {
        JobReqDto jobReqDto = new JobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Account.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Account.toString());
        jobReqDto.setCron(cron);
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setAccountId(accountId);
        jobReqDto.setJobParamDto(jobParamDto);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addSpotJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiSpotCurrentPriceJob(String symbol, String cron, boolean enable) {
        JobReqDto jobReqDto = new JobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.CurrentPrice.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.CurrentPrice.toString());
        jobReqDto.setCron(cron);
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setSymbol(symbol);
        jobReqDto.setJobParamDto(jobParamDto);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addSpotJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiSpotDepthJob(String symbol, String depthType, String cron, boolean enable) {
        JobReqDto jobReqDto = new JobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Depth.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Depth.toString());
        jobReqDto.setCron(cron);
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setSymbol(symbol);
        jobParamDto.setDepthType(depthType);
        jobReqDto.setJobParamDto(jobParamDto);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addSpotJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiSpotKlineJob(String symbol, String klineType, int size, String cron, boolean enable) {
        JobReqDto jobReqDto = new JobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Kline.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Kline.toString());
        jobReqDto.setCron(cron);
        JobParamDto jobParamDto = new JobParamDto();
        jobParamDto.setSymbol(symbol);
        jobParamDto.setKlineType(klineType);
        jobParamDto.setSize(size);
        jobReqDto.setJobParamDto(jobParamDto);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addSpotJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiSpotOrderJob(String cron, boolean enable) {
        JobReqDto jobReqDto = new JobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Order.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Order.toString());
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addSpotJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiFuturePositionJob(Long accountId, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Position.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Position.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setAccountId(accountId);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }

    @Override
    public ServiceResult addHuobiFutureUserInfoJob(Long accountId, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        jobReqDto.setJobType(HuobiJobTypeEnum.Account.getJobType());
        jobReqDto.setJobDesc(HuobiJobTypeEnum.Account.toString());
        JobParamDto paramDto = new JobParamDto();
        paramDto.setAccountId(accountId);

        jobReqDto.setJobParamDto(paramDto);
        jobReqDto.setCron(cron);
        if (enable) {
            jobReqDto.setState(1);
        } else {
            jobReqDto.setState(0);
        }
        return addFutureJob(jobReqDto);
    }
}
