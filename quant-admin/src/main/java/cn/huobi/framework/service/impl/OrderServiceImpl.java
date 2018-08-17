package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.entity.StrategyRiskConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyOrderConfig;

import cn.huobi.framework.dao.OrderDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.service.OrderService;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	
	@Resource
	private StrategyOrderConfigMapper strategyOrderConfigMapper;

	@Override
	public int updateOrder(StrategyOrderConfig config) {
		return strategyOrderConfigMapper.updateByPrimaryKeySelective(config);
	}

	@Override
	public PageInfo<StrategyOrderConfig> selectByCondition(StrategyOrderConfig config, PageInfo<StrategyOrderConfig> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<StrategyOrderConfig> strategyOrderConfigs = strategyOrderConfigMapper.selectList(config);
		page = new PageInfo<>(strategyOrderConfigs);
		return page;
	}

}
