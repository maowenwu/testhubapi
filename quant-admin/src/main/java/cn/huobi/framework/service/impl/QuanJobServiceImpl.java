package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.QuanJobMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.QuanJob;
import com.huobi.quantification.enums.ExchangeEnum;

import cn.huobi.framework.dao.QuanJobDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;
import cn.huobi.framework.service.QuanJobService;

@Service("quanJobService")
@Transactional
public class QuanJobServiceImpl implements QuanJobService {
	
	@Autowired
	private QuanJobMapper quanJobMapper;

	private QuanJob convertSpotJob(SpotJob job) {
		QuanJob quanJob = new QuanJob();
		quanJob.setJobDesc(job.getJobDesc());
		if (StringUtils.isNotBlank(job.getExchangeId())) {
			quanJob.setExchangeId(Integer.parseInt(job.getExchangeId()));
		}
		return quanJob;
	}

	private SpotJob convertQuanJob2SpotJob(QuanJob job) {
		SpotJob spotJob = new SpotJob();
		spotJob.setCron(job.getCron());
		spotJob.setExchangeId(ExchangeEnum.valueOf(job.getExchangeId()).getExName());
		spotJob.setId(job.getId());
		spotJob.setJobDesc(job.getJobDesc());
		spotJob.setJobName(job.getJobName());
		spotJob.setJobParam(job.getJobParam());
		spotJob.setJobType(job.getJobType());
		Integer state = job.getState();
		if (state != 0) {
			spotJob.setState("开启");
		}else {
			spotJob.setState("关闭");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		spotJob.setUpdateDate(formatter.format(job.getUpdateDate()));
		spotJob.setCreateDate(formatter.format(job.getCreateDate()));
		return spotJob;
	}


	@Override
	public int updateSpotJob(Boolean status, Integer id) {
		QuanJob quanJob = new QuanJob();
		quanJob.setUpdateDate(new Date());
		quanJob.setId(id);
		if (status == null || !status) {
			quanJob.setState(0);
		}else {
			quanJob.setState(1);
		}
		return quanJobMapper.updateByPrimaryKeySelective(quanJob);
	}

	@Override
	public PageInfo<SpotJob> selectByCondition(SpotJob job, PageInfo<SpotJob> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		QuanJob quanJob = convertSpotJob(job);
		List<QuanJob> jobs = quanJobMapper.selectList(quanJob);
		List<SpotJob> spotJobs = new ArrayList<>();
		jobs.stream().forEach(e -> {
			spotJobs.add(convertQuanJob2SpotJob(e));
		});
		page = new PageInfo<>(spotJobs);
		com.github.pagehelper.Page totalPage = (com.github.pagehelper.Page) jobs;
		page.setTotal(totalPage.getTotal());
		return page;
	}
}
