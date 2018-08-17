package cn.huobi.framework.service.impl;

import cn.huobi.framework.model.FinanceHistory;
import cn.huobi.framework.service.FinanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.StrategyFinanceHistoryMapper;
import com.huobi.quantification.entity.StrategyFinanceHistory;
import com.huobi.quantification.enums.ExchangeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("financeService")
@Transactional
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private StrategyFinanceHistoryMapper strategyFinanceHistoryMapper;

    private FinanceHistory convertStrategyFinanceHistory(StrategyFinanceHistory strategyFinanceHistory) {
        FinanceHistory financeHistory = new FinanceHistory();
        financeHistory.setAccountId(strategyFinanceHistory.getAccountId());
        financeHistory.setCoinType(strategyFinanceHistory.getCoinType());
        financeHistory.setExchangeId(ExchangeEnum.valueOf(strategyFinanceHistory.getExchangeId()).getExName());
        if (strategyFinanceHistory.getMoneyType() == 1) {
            financeHistory.setMoneyType("充值");
        } else if (strategyFinanceHistory.getMoneyType() == 2) {
            financeHistory.setMoneyType("提现");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        financeHistory.setCreateTime(formatter.format(strategyFinanceHistory.getCreateTime()));
        financeHistory.setUpdateTime(formatter.format(strategyFinanceHistory.getUpdateTime()));
        financeHistory.setTransferAmount(strategyFinanceHistory.getTransferAmount());
        return financeHistory;
    }

    private StrategyFinanceHistory convertFinanceHistory(FinanceHistory history) {
        StrategyFinanceHistory strategyFinanceHistory = new StrategyFinanceHistory();
        strategyFinanceHistory.setAccountId(history.getAccountId());
        strategyFinanceHistory.setCoinType(history.getCoinType());
        if (StringUtils.isNotBlank(history.getExchangeId())) {
            strategyFinanceHistory.setExchangeId(Integer.parseInt(history.getExchangeId()));
        }
        return strategyFinanceHistory;
    }

    @Override
    public int insert(StrategyFinanceHistory history) {
        history.setInit(0);
        history.setCreateTime(new Date());
        history.setUpdateTime(new Date());
        return strategyFinanceHistoryMapper.insert(history);
    }

    @Override
    public PageInfo<FinanceHistory> selectByCondition(FinanceHistory history, PageInfo<FinanceHistory> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        StrategyFinanceHistory strategyFinanceHistory = convertFinanceHistory(history);
        List<StrategyFinanceHistory> histories = strategyFinanceHistoryMapper.selectList(strategyFinanceHistory);
        List<FinanceHistory> financeHistories = new ArrayList<>();
        histories.stream().forEach(e->{
            financeHistories.add(convertStrategyFinanceHistory(e));

        });
        page = new PageInfo<>(financeHistories);
        com.github.pagehelper.Page totalPage = (com.github.pagehelper.Page) histories;
        page.setTotal(totalPage.getTotal());
        return page;
    }

}
