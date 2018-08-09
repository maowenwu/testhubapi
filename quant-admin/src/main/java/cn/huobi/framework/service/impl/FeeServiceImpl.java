package cn.huobi.framework.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.entity.StrategyTradeFee;

import cn.huobi.framework.dao.FeeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;

@Service("feeService")
@Transactional
public class FeeServiceImpl implements FeeService {
	
	@Resource
	private FeeDao	feeDao;

	@Override
	public List<TradeFee> selectByCondition(TradeFee tradeFee, Page<TradeFee> page) {
		StrategyTradeFee fee = convertTradeFee(tradeFee);
		List<StrategyTradeFee> fees = feeDao.selectByCondition(fee, page);
		List<TradeFee> tradeFees = new ArrayList<>();
		for (StrategyTradeFee strategyTradeFee : fees) {
			TradeFee sTradeFee = convertStrategyTradeFee(strategyTradeFee);
			tradeFees.add(sTradeFee);
		}
		return tradeFees;
	}

	private TradeFee convertStrategyTradeFee(StrategyTradeFee strategyTradeFee) {
		TradeFee tradeFee = new TradeFee();
		tradeFee.setContractFee(strategyTradeFee.getContractFee());
		tradeFee.setContractType(strategyTradeFee.getContractType());
		tradeFee.setDeliveryFee(strategyTradeFee.getDeliveryFee());
		tradeFee.setId(strategyTradeFee.getId());
		tradeFee.setSpotFee(strategyTradeFee.getSpotFee());
		tradeFee.setSymbol(strategyTradeFee.getSymbol());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tradeFee.setCreateTime(formatter.format(strategyTradeFee.getCreateTime()));
		tradeFee.setUpdateTime(formatter.format(strategyTradeFee.getUpdateTime()));
		return tradeFee;
	}

	private StrategyTradeFee convertTradeFee(TradeFee tradeFee) {
		StrategyTradeFee strategyTradeFee = new StrategyTradeFee();
		strategyTradeFee.setId(tradeFee.getId());
		strategyTradeFee.setContractFee(tradeFee.getContractFee());
		strategyTradeFee.setSpotFee(tradeFee.getSpotFee());
		strategyTradeFee.setDeliveryFee(tradeFee.getDeliveryFee());
		strategyTradeFee.setUpdateTime(new Date());
		strategyTradeFee.setSymbol(tradeFee.getSymbol());
		strategyTradeFee.setContractType(tradeFee.getContractType());
		return strategyTradeFee;
	}

	@Override
	public int updateHedge(TradeFee fee) {
		StrategyTradeFee tradeFee = convertTradeFee(fee);
		return feeDao.update(tradeFee);
	}

}
