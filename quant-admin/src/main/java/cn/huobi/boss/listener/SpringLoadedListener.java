package cn.huobi.boss.listener;

import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.huobi.boss.quartz.QuartzManager;

/**
 * spring容器加载完后启动的监听器
 * 目的： 服务器重新启动，需要将出款服务定时任务重新启动
 * @author Administrator
 *
 */
public class SpringLoadedListener  implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private QuartzManager quartzManager;

	private final Logger log = LoggerFactory.getLogger(SpringLoadedListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			quartzManager.startJobs();
		} catch (Exception e) {
			log.error("spring容器初始化定时任务出现异常", e);
		}
	}

}
