package com.huobi.quantification.job.huobi.spot;

import com.huobi.quantification.dto.JobParamDto;
import org.quartz.DisallowConcurrentExecution;

import com.huobi.quantification.common.context.ApplicationContextHolder;
import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.job.AbstractQuartzJob;
import com.huobi.quantification.service.market.HuobiMarketService;

@DisallowConcurrentExecution
public class HuobiKlineJob extends AbstractQuartzJob{

	@Override
	public void execute(JobParamDto data) {
		HuobiMarketService marketHuobiService = ApplicationContextHolder.getContext().getBean(HuobiMarketService.class);
		marketHuobiService.updateKline(data.getSymbol(), data.getKlineType(), data.getSize()+"");
	}

}
