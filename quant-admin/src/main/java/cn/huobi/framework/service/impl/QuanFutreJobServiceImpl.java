package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.QuanJobFutureMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.QuanJobFuture;
import com.huobi.quantification.enums.ExchangeEnum;

import cn.huobi.framework.dao.QuanFutureJobDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureJob;
import cn.huobi.framework.service.QuanFutureJobService;

@Service("quanFutureJobService")
@Transactional
public class QuanFutreJobServiceImpl implements QuanFutureJobService{
	
	@Autowired
	private QuanJobFutureMapper quanJobFutureMapper;

	private QuanJobFuture convertFutureJob(FutureJob job) {
		QuanJobFuture quanJobFuture = new QuanJobFuture();
		if (StringUtils.isNotBlank(job.getExchangeId())) {
			quanJobFuture.setExchangeId(Integer.parseInt(job.getExchangeId()));
		}
		quanJobFuture.setJobDesc(job.getJobDesc());
		return quanJobFuture;
	}

	private FutureJob convertQuanJobFuture2FutureJob(QuanJobFuture quanJobFuture) {
		FutureJob futureJob = new FutureJob();
		futureJob.setCron(quanJobFuture.getCron());
		futureJob.setExchangeId(ExchangeEnum.valueOf(quanJobFuture.getExchangeId()).getExName());
		futureJob.setId(quanJobFuture.getId());
		futureJob.setJobDesc(quanJobFuture.getJobDesc());
		futureJob.setJobName(quanJobFuture.getJobName());
		futureJob.setJobParam(quanJobFuture.getJobParam());
		futureJob.setJobType(quanJobFuture.getJobType());
		Integer state = quanJobFuture.getState();
		if (state != 0) {
			futureJob.setState("开启");
		}else {
			futureJob.setState("关闭");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		futureJob.setUpdateDate(formatter.format(quanJobFuture.getUpdateDate()));
		futureJob.setCreateDate(formatter.format(quanJobFuture.getCreateDate()));
		return futureJob;
	}

	@Override
	public int updateFutureJob(Boolean status, Integer id) {
		QuanJobFuture quanJobFuture = new QuanJobFuture();
		quanJobFuture.setUpdateDate(new Date());
		quanJobFuture.setId(id);
		if (status == null || !status) {
			quanJobFuture.setState(0);
		}else {
			quanJobFuture.setState(1);
		}
		return quanJobFutureMapper.updateByPrimaryKeySelective(quanJobFuture);
	}

	@Override
	public PageInfo<FutureJob> selectJobByCondition(FutureJob job, PageInfo<FutureJob> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		QuanJobFuture quanJobFuture = convertFutureJob(job);
		List<QuanJobFuture> quanJobFutures = quanJobFutureMapper.selectList(quanJobFuture);
		List<FutureJob> futureJobs = new ArrayList<>();
        quanJobFutures.stream().forEach( e -> {
            FutureJob futureJob = convertQuanJobFuture2FutureJob(e);
            futureJobs.add(futureJob);
        });
        page = new PageInfo<>(futureJobs);
        return page;
	}

}
