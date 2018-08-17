package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureJob;
import com.github.pagehelper.PageInfo;

public interface QuanFutureJobService {

	int updateFutureJob(Boolean status, Integer id);

	PageInfo<FutureJob> selectJobByCondition(FutureJob job, PageInfo<FutureJob> page);
}
