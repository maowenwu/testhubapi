package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;

public interface FeeService {

	List<TradeFee> selectByCondition(TradeFee tradeFee, Page<TradeFee> page);

	int updateHedge(TradeFee fee);

}
