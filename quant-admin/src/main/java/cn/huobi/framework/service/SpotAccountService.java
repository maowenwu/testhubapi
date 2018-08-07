package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotAccount;

public interface SpotAccountService {

	List<SpotAccount> selectByCondition(SpotAccount account, Page<SpotAccount> page);

	int insert(SpotAccount account);

	int update(SpotAccount account);

	int deleteById(Integer id);

}
