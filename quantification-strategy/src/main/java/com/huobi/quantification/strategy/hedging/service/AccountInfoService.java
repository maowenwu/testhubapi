package com.huobi.quantification.strategy.hedging.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.dto.SpotBalanceRespDto.DataBean;
import com.huobi.quantification.strategy.order.entity.FutureBalance;

/**
 * 获取账户信息
 * 
 * @author maowenwu
 */
@Component
public class AccountInfoService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SpotAccountService spotAccountService;

	@Autowired
	FutureAccountService futureAccountService;

	/**
	 * 获取Huobi现货账户指定账户期末(即当前)余额
	 * @param spotBalanceReqDto
	 * @param coinType
	 * @return
	 */
	public DataBean getHuobiSpotCurrentBalance(Long accountId, int exchangeID, String coinType) {
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(accountId);
		spotBalanceReqDto.setExchangeId(exchangeID);
		spotBalanceReqDto.setMaxDelay(1000 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(spotBalanceReqDto);
		logger.info("获取火币现货币币账户余额,币种： {}  的期末余额为：{} ", coinType, JSON.toJSONString(result));
		DataBean dataBean = result.getData().getData().get(coinType);
		return dataBean;
	}

	/**
	 * 查询huobi期货账户基本信息
	 * 
	 * @param futureBalanceReqDto
	 * @return
	 */
	public FutureBalance getHuobiFutureBalance(Long accountId,int exchangeId,String contractCode) {
		FutureBalanceReqDto futureBalanceReqDto=new FutureBalanceReqDto();
		futureBalanceReqDto.setAccountId(accountId);
		futureBalanceReqDto.setExchangeId(exchangeId);
		
		ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(futureBalanceReqDto);
		Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
		FutureBalanceRespDto.DataBean dataBean = data.get(contractCode);
		if (dataBean != null) {
			FutureBalance futureBalance = new FutureBalance();
			BeanUtils.copyProperties(dataBean, futureBalance);
			return futureBalance;
		} else {
			return null;
		}
	}

}