package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureAccount;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.QuanAccountFuture;

public interface FutureAccountService {

	int insert(QuanAccountFuture account);

	int update(QuanAccountFuture account);

	int deleteById(Long id);

	PageInfo<QuanAccountFuture> selectByCondition(QuanAccountFuture account, PageInfo<QuanAccountFuture> page);
}
