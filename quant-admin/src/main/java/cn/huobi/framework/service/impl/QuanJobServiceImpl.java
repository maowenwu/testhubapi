package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.QuanJob;

import cn.huobi.framework.dao.QuanJobDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;
import cn.huobi.framework.service.QuanJobService;

@Service("quanJobService")
@Transactional
public class QuanJobServiceImpl implements QuanJobService {
	
	@Autowired
	private QuanJobDao quanJobDao;

	@Override
	public List<SpotJob> selectByCondition(SpotJob job, Page<SpotJob> page) {
		QuanJob quanJob = convertSpotJob(job);
		List<QuanJob> jobs = quanJobDao.selectByCondition(quanJob, page);
		List<SpotJob> spotJobs = new ArrayList<>();
		for (QuanJob job2 : jobs) {
			SpotJob spotJob = convertQuanJob2SpotJob(job2);
			spotJobs.add(spotJob);
		}
		return spotJobs;
	}

	private QuanJob convertSpotJob(SpotJob job) {
		QuanJob quanJob = new QuanJob();
		quanJob.setExchangeId(job.getExchangeId());
		quanJob.setJobDesc(job.getJobDesc());
		return quanJob;
	}

	private SpotJob convertQuanJob2SpotJob(QuanJob job) {
		SpotJob spotJob = new SpotJob();
		spotJob.setCron(job.getCron());
		spotJob.setExchangeId(job.getExchangeId());
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
	public int deleteById(Integer id) {
		return quanJobDao.delete(id);
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
		return quanJobDao.update(quanJob);
	}
}
