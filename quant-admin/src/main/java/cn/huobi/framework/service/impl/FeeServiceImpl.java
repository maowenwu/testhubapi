package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.FeeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;
import com.huobi.quantification.entity.StrategyTradeFee;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("feeService")
@Transactional
public class FeeServiceImpl implements FeeService {
	
	@Resource
	private FeeDao feeDao;

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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (StringUtils.isNotBlank(tradeFee.getCreateTime())){
				strategyTradeFee.setCreateTime(formatter.parse(tradeFee.getCreateTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strategyTradeFee;
	}

	@Override
	public int updateHedge(TradeFee fee) {
		StrategyTradeFee tradeFee = convertTradeFee(fee);
		return feeDao.update(tradeFee);
	}

}
