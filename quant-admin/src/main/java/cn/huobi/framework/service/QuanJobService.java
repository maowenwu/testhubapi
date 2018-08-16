package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;
import com.github.pagehelper.PageInfo;

public interface QuanJobService {

	int updateSpotJob(Boolean status, Integer id);

	PageInfo<SpotJob> selectByCondition(SpotJob job, PageInfo<SpotJob> page);
}
