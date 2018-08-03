package com.huobi.quantification.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.service.redis.RedisService;

/**
 * 用于JobManagerService接口的测试
 * @author lichenyang
 * @since  2018年7月20日
 */
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class JobManagerServiceTest {
	
	@Autowired
	private RedisService redisService;
	
	@Test
	public void getAccountHistory() {
		QuanAccountFuturePosition futurePosition = redisService.getFuturePosition(0, 101L);
	   System.out.println("==============="+futurePosition);
	}
}
