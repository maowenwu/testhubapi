package com.huobi.quantification.job.huobicoin;

import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.MarketHuobiService;

@DisallowConcurrentExecution
public class HuobiTickerJob extends AbstractQuartzJob{

	@Override
	public void execute(Object data) {
		MarketHuobiService marketHuobiService = ApplicationContextHolder.getContext().getBean(MarketHuobiService.class);
		if (data instanceof QuanJob) {
			QuanJob jobdata = (QuanJob)data;
			marketHuobiService.updateHuobiTicker(jobdata.getSymbol());
		}
	}

}
