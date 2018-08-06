package cn.huobi.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

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
	private OrderDao orderDao;

	@Override
	public List<StrategyOrderConfig> selectByCondition(StrategyOrderConfig config, Page<StrategyOrderConfig> page) {
		return orderDao.selectByCondition(config, page);
	}

	@Override
	public int updateOrder(StrategyOrderConfig config) {
		return orderDao.update(config);
	}

}
