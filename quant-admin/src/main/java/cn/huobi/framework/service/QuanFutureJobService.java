package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureJob;

public interface QuanFutureJobService {

	List<FutureJob> selectDicByCondition(FutureJob job, Page<FutureJob> page);

	int updateFutureJob(Boolean status, Integer id);

}
