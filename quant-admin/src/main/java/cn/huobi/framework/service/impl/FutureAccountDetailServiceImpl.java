package cn.huobi.framework.service.impl;

import cn.huobi.framework.service.FutureAccountDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.*;
import com.huobi.quantification.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("futureAccountDetailService")
@Transactional
public class FutureAccountDetailServiceImpl implements FutureAccountDetailService {

    @Autowired
    private QuanAccountFutureMapper quanAccountFutureMapper;

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Autowired
    private StrategyFinanceHistoryMapper strategyFinanceHistoryMapper;

    @Autowired
    private QuanOrderFutureMapper quanOrderFutureMapper;

    @Override
    public PageInfo<QuanAccountFuturePosition> selectAccountPosition(QuanAccountFuturePosition quanAccountFuturePosition, PageInfo<QuanAccountFuturePosition> page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        if (null != quanAccountFuturePosition.getBaseCoin()){
            quanAccountFuturePosition.setBaseCoin(quanAccountFuturePosition.getBaseCoin().toUpperCase());
        }
        List<QuanAccountFuturePosition> quanAccountFuturePositions = quanAccountFuturePositionMapper.selectByCondition(quanAccountFuturePosition);
        page = new PageInfo<>(quanAccountFuturePositions);
        return page;
    }

    @Override
    public int insertFinance(StrategyFinanceHistory history) {
        history.setInit(0);
        history.setCreateTime(new Date());
        history.setUpdateTime(new Date());
        if (history.getMoneyType() == 2){
            history.setTransferAmount(new BigDecimal("0").subtract(history.getTransferAmount()));
        }
        return strategyFinanceHistoryMapper.insert(history);
    }

    @Override
    public PageInfo<StrategyFinanceHistory> selectFinanceHistory(BossFinanceQuery bossFinanceQuery, PageInfo<StrategyFinanceHistory> page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        if (bossFinanceQuery == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(calendar.MONTH,-1);
            bossFinanceQuery.setStartDate(calendar.getTime());
            bossFinanceQuery.setEndDate(new Date());
        }
        List<StrategyFinanceHistory> financeHistories = strategyFinanceHistoryMapper.selectList(bossFinanceQuery);
        financeHistories.stream().forEach(e -> {
            if (e.getTransferAmount().compareTo(new BigDecimal("0")) < 0) {
                e.setTransferAmount(new BigDecimal("0").subtract(e.getTransferAmount()));
            }
        });
        page = new PageInfo<>(financeHistories);
        return page;
    }

    @Override
    public List<QuanAccountFutureAsset> selectAccountDetail(QuanAccountFuture quanAccountFuture) {
        List<QuanAccountFutureAsset> quanAccountFutureAssets = quanAccountFutureAssetMapper.selectNewestAssets(quanAccountFuture);
        return quanAccountFutureAssets;
    }

    @Override
    public QuanAccountFuture selectAccountId(QuanAccountFuture quanAccountFuture) {
        long accountId = quanAccountFutureMapper.selectAccountFutureId(quanAccountFuture.getExchangeId(),quanAccountFuture.getAccountSourceId());
        quanAccountFuture.setId(accountId);
        return quanAccountFuture;
    }

    @Override
    public PageInfo<QuanOrderFuture> selectAccountOrder(List<Integer> sourceStatus, QuanOrderFuture quanOrderFuture, PageInfo<QuanOrderFuture> page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        return null;
    }

    @Override
    public PageInfo<QuanOrderFuture> selectAccountOrder(QuanOrderFuture quanOrderFuture, PageInfo<QuanOrderFuture> page) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<QuanOrderFuture> quanOrderFutures = quanOrderFutureMapper.selectList(quanOrderFuture);
        page = new PageInfo<>(quanOrderFutures);
        return page;
    }
}
