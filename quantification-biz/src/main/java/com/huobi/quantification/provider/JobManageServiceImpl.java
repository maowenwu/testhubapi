package com.huobi.quantification.provider;

import java.util.Date;

import com.huobi.quantification.enums.ExchangeEnum;
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
        ServiceResult result = new ServiceResult();
        try {
            updateJobFuture(jobReqDto.getExchangeId(), jobReqDto.getJobType(),
                    jobReqDto.getJobParamDto(), jobReqDto.getCron(), jobReqDto.getState());
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

    private void updateJob(int exchangeId, int jobType, JobParamDto jobParamDto, String cron, int state) {
        QuanJob jobFuture = new QuanJob();
        jobFuture.setExchangeId(exchangeId);
        jobFuture.setJobType(jobType);
        // JobName需要唯一
        jobFuture.setJobName(genJobName(exchangeId, jobType, jobParamDto));
        jobFuture.setJobParam(JSON.toJSONString(jobParamDto));
        jobFuture.setCron(cron);
        jobFuture.setState(state);
        jobFuture.setCreateDate(new Date());
        jobFuture.setUpdateDate(new Date());
        quanJobMapper.insertOrUpdate(jobFuture);
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
	public ServiceResult startSpotJob(JobReqDto jobReqDto) {
		ServiceResult result = new ServiceResult();
		try {
			updateJob(jobReqDto.getExchangeId(), jobReqDto.getJobType(), jobReqDto.getJobParamDto(),
					jobReqDto.getCron(), 1);
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
    public ServiceResult addOkFutureCurrentPriceJob(String symbol, String contractType, String cron, boolean enable) {
        FutureJobReqDto jobReqDto = new FutureJobReqDto();
        jobReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
        jobReqDto.setJobType(OkJobTypeEnum.CurrentPrice.getJobType());

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
	public ServiceResult stopSpotJob(JobReqDto jobReqDto) {
		ServiceResult result = new ServiceResult();
		try {
			updateJob(jobReqDto.getExchangeId(), jobReqDto.getJobType(), jobReqDto.getJobParamDto(),
					jobReqDto.getCron(), 0);
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
}
