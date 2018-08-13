package cn.huobi.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huobi.quantification.enums.ExchangeEnum;

import cn.huobi.framework.dao.SpotAccountDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.QuanSpotAccount;
import cn.huobi.framework.model.SpotAccount;
import cn.huobi.framework.service.SpotAccountService;

@Service("spotAccountService")
@Transactional
public class SpotAccountServiceImpl implements SpotAccountService{
	
	@Resource
	private SpotAccountDao spotAccountDao;

	@Override
	public List<SpotAccount> selectByCondition(SpotAccount account, Page<SpotAccount> page) {
		QuanSpotAccount quanAccount = covertSpotAccout(account);
		List<QuanSpotAccount> quanSpotAccounts = spotAccountDao.selectByCondition(quanAccount, page);
		List<SpotAccount> quAccounts = new ArrayList<>();
		for (QuanSpotAccount quanSpotAccount : quanSpotAccounts) {
			SpotAccount spotAccount = convertQuanSpotAccount(quanSpotAccount);
			quAccounts.add(spotAccount);
		}
		return quAccounts;
	}

	private SpotAccount convertQuanSpotAccount(QuanSpotAccount quanSpotAccount) {
		SpotAccount spotAccount = new SpotAccount();
		spotAccount.setAccountsName(quanSpotAccount.getAccountsName());
		spotAccount.setAccountSourceId(quanSpotAccount.getAccountSourceId());
		spotAccount.setAccountsType(quanSpotAccount.getAccountsType());
		spotAccount.setExchangeId(ExchangeEnum.valueOf(quanSpotAccount.getExchangeId()).getExName());
		spotAccount.setId(quanSpotAccount.getId());
		spotAccount.setState(quanSpotAccount.getState());
		return spotAccount;
	}

	private QuanSpotAccount covertSpotAccout(SpotAccount account) {
		QuanSpotAccount quanSpotAccount = new QuanSpotAccount();
		if (StringUtils.isNotBlank(account.getExchangeId())) {
			quanSpotAccount.setExchangeId(Integer.parseInt(account.getExchangeId()));
		}
		quanSpotAccount.setAccountsName(account.getAccountsName());
		quanSpotAccount.setAccountSourceId(account.getAccountSourceId());
		quanSpotAccount.setAccountsType(account.getAccountsType());
		quanSpotAccount.setState(account.getState());
		quanSpotAccount.setId(account.getId());
		return quanSpotAccount;
	}

	@Override
	public int insert(SpotAccount account) {
		QuanSpotAccount covertSpotAccout = covertSpotAccout(account);
		return spotAccountDao.insert(covertSpotAccout);
	}

	@Override
	public int update(SpotAccount account) {
		QuanSpotAccount covertSpotAccout = covertSpotAccout(account);
		return spotAccountDao.update(covertSpotAccout);
	}

	@Override
	public int deleteById(Integer id) {
		return spotAccountDao.delete(id);
	}

}
