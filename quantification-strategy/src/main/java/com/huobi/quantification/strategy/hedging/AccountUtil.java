package com.huobi.quantification.strategy.hedging;

import java.math.BigDecimal;
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

@Component
public class AccountUtil {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SpotAccountService spotAccountService;

	@Autowired
	FutureAccountService futureAccountService;

	// 计算火币现货币币账户期末(即当前)余额USDT

	public DataBean getHuobiSpotCurrentUSDTBalance(SpotBalanceReqDto spotBalanceReqDto, String coinType) {
		logger.info("获取火币现货币币账户请求参数为 {}   {}  ",coinType, JSON.toJSONString(spotBalanceReqDto));
		ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(spotBalanceReqDto);
		logger.info("获取火币现货币币账户,币种： {}  的期末余额为：{} ",coinType, JSON.toJSONString(result));
		DataBean dataBean = result.getData().getData().get(coinType);
		return dataBean;
	}

	/**
	 * 查询OK账户指定币种基本信息
	 * 
	 * @param futureBalanceReqDto
	 * @return
	 */
	public FutureBalance getOkFutureBalance(FutureBalanceReqDto futureBalanceReqDto) {
		ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(futureBalanceReqDto);
		Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
		FutureBalanceRespDto.DataBean dataBean = data.get(futureBalanceReqDto.getCoinType());
		if (dataBean != null) {
			FutureBalance futureBalance = new FutureBalance();
			BeanUtils.copyProperties(dataBean, futureBalance);
			return futureBalance;
		} else {
			return null;
		}
	}
	
	//计算净寸头
	public BigDecimal calPosition(BigDecimal spotCurrentBalance,BigDecimal spotLastBalance,BigDecimal futureCurrentBalance,BigDecimal futureLastBalance,BigDecimal rate) {
		BigDecimal total1=(spotCurrentBalance.subtract(spotLastBalance)).multiply(rate);
		BigDecimal total2=(futureCurrentBalance.subtract(futureLastBalance));		
		return total1.subtract(total2);
	}
	
	
	
	

}
