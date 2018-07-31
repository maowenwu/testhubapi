package com.huobi.quantification.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.api.future.JobManageService;

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
//		jobManageService.addHuobiSpotDepthJob("ethusdt", "step1", "0/1 * * * * ?", true);
//		jobManageService.addHuobiSpotCurrentPriceJob("ethusdt", "0/1 * * * * ?", true);
//		jobManageService.addHuobiSpotKlineJob("ethusdt", "1day", 200, "0/1 * * * * ?", true);
		//jobManageService.addHuobiSpotOrderJob("0/1 * * * * ?", true);
//		jobManageService.addHuobiSpotAccountJob(4232061L, "0/1 * * * * ?", true);
	}
}
