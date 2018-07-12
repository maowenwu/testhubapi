package cn.huobi.framework.service.sysUser.impl;

import cn.huobi.framework.dao.BossOperLogDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.BossOperLog;
import cn.huobi.framework.service.BossOperLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("bossOperLogService")
@Transactional
public class BossOperLogServiceImpl implements BossOperLogService {
	
	private static final Logger log = LoggerFactory.getLogger(BossOperLogServiceImpl.class);
	
	@Resource
	private BossOperLogDao bossOperLogDao;

	@Override
	public int insert(BossOperLog bossOperLog) {
		// TODO Auto-generated method stub
		return bossOperLogDao.insert(bossOperLog);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<BossOperLog> queryByCondition(Page page, BossOperLog logInfo) {
		return bossOperLogDao.queryByCondition(page, logInfo);
	}
	
	@Override
	public BossOperLog queryDetail(Integer id) {
		return bossOperLogDao.queryDetail(id);
	}
}
