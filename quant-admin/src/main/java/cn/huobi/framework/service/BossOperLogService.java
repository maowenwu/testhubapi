package cn.huobi.framework.service;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.BossOperLog;

import java.util.List;

/**
 * BossOperLogService
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:33:02
 */
public interface BossOperLogService {

	public  int insert(BossOperLog bossOperLog);
	
	BossOperLog queryDetail(Integer id);

	List<BossOperLog> queryByCondition(Page page, BossOperLog logInfo);
}
