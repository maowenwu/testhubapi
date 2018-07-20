package com.huobi.quantification.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.api.future.JobManageService;
import com.huobi.quantification.dto.JobParamDto;
import com.huobi.quantification.dto.JobReqDto;
import com.huobi.quantification.enums.ExchangeEnum;

/**
 * 用于JobManagerService接口的测试
 * @author lichenyang
 * @since  2018年7月20日
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class JobManagerServiceTest {
	
	@Autowired
	private JobManageService jobManageService;
	
	@Test
	public void testAddJob() {
		JobReqDto JobReqDto = new JobReqDto();
		JobReqDto.setCron("0/1 * * * * * ?");
		JobReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		JobReqDto.setJobType(1);
		JobParamDto jobParamDto = new JobParamDto();
		jobParamDto.setDepthType("step1");
		jobParamDto.setSymbol("ethusdt");
		JobReqDto.setJobParamDto(jobParamDto);
		jobManageService.startSpotJob(JobReqDto);
	}
}
