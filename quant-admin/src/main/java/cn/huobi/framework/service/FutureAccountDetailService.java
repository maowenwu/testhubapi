package cn.huobi.framework.service;

import cn.huobi.framework.model.FutureAccount;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.*;

import java.util.List;

public interface FutureAccountDetailService {

    PageInfo<QuanAccountFuturePosition> selectAccountPosition(QuanAccountFuturePosition quanAccountFuturePosition, PageInfo<QuanAccountFuturePosition> page);

    int insertFinance(StrategyFinanceHistory history);

    PageInfo<StrategyFinanceHistory> selectFinanceHistory(BossFinanceQuery bossFinanceQuery, PageInfo<StrategyFinanceHistory> page);

    PageInfo<QuanOrderFuture> selectAccountOrder(QuanOrderFuture quanOrderFuture, PageInfo<QuanOrderFuture> page);

    List<QuanAccountFutureAsset> selectAccountDetail(QuanAccountFuture quanAccountFuture);

    QuanAccountFuture selectAccountId(QuanAccountFuture quanAccountFuture);

    PageInfo<QuanOrderFuture> selectAccountOrder(List<Integer> sourceStatus, QuanOrderFuture quanOrderFuture, PageInfo<QuanOrderFuture> page);
}
