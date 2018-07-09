package com.huobi.quantification.job.huobi;

import org.quartz.DisallowConcurrentExecution;
import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.HuobiMarketService;

@DisallowConcurrentExecution
public class HuobiDepthJob extends AbstractQuartzJob{

	@Override
	public void execute(Object data) {
		HuobiMarketService marketHuobiService = ApplicationContextHolder.getContext().getBean(HuobiMarketService.class);
		if (data instanceof QuanJob) {
			QuanJob jobData = (QuanJob)data;
			marketHuobiService.updateHuobiDepth(jobData.getSymbol(),jobData.getType());
		}
	}

}
