package com.huobi.quantification.service.api;

import com.huobi.quantification.ServiceApplication;
import com.huobi.quantification.service.redis.RedisService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
	

}
