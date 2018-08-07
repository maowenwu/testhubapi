package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureAccount;

public interface FutureAccountService {

	List<FutureAccount> selectByCondition(FutureAccount account, Page<FutureAccount> page);

	int insert(FutureAccount account);

	int update(FutureAccount account);

	int deleteById(Integer id);

}
