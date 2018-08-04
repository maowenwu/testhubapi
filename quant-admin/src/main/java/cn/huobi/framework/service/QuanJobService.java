package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;

public interface QuanJobService {
	List<SpotJob> selectDicByCondition(SpotJob job, Page<SpotJob> page);

	int deleteById(Integer id);

	int updateSpotJob(Boolean status, Integer id);
}
