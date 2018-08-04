package com.huobi.quantification.strategy.hedging.service;

import java.math.BigDecimal;
import java.util.List;
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
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.dto.SpotBalanceRespDto.DataBean;
import com.huobi.quantification.strategy.order.entity.FutureBalance;
import com.huobi.quantification.strategy.order.entity.FuturePosition;

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
	 * 获取Huobi现货账户指定账户信息
	 *
	 * @param spotBalanceReqDto
	 * @param coinType
	 * @return
	 */
	private DataBean getBalance(Long accountId, int exchangeID, String coinType) {
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
	public FutureBalance getHuobiFutureBalance(Long accountId, Integer exchangeId, String coinType) {
		FutureBalanceReqDto futureBalanceReqDto = new FutureBalanceReqDto();
		futureBalanceReqDto.setAccountId(accountId);
		futureBalanceReqDto.setExchangeId(exchangeId);
		futureBalanceReqDto.setTimeout(100);
		futureBalanceReqDto.setMaxDelay(3000);
		ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(futureBalanceReqDto);
		Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
		FutureBalanceRespDto.DataBean dataBean = data.get(coinType);
		if (dataBean == null) {
			dataBean = data.get(coinType.toUpperCase());
		}
		if (dataBean != null) {
			FutureBalance futureBalance = new FutureBalance();
			BeanUtils.copyProperties(dataBean, futureBalance);
			return futureBalance;
		} else {
			return null;
		}
	}

	/**
	 * 获取期货持仓信息
	 *
	 * @param accountId
	 * @param exchangeId
	 * @param contractCode
	 */
	public FuturePosition getFuturePosition(Long accountId, Integer exchangeId, String contractCode) {
		try {
			FuturePositionReqDto reqDto = new FuturePositionReqDto();
			reqDto.setExchangeId(exchangeId);
			reqDto.setAccountId(accountId);
			reqDto.setCoinType(contractCode);
			reqDto.setTimeout(100);
			reqDto.setMaxDelay(3000);
			ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(reqDto);
			Map<String, List<FuturePositionRespDto.DataBean>> data = position.getData().getData();
			List<FuturePositionRespDto.DataBean> beanList = data.get(contractCode);
			FuturePosition futurePosition = new FuturePosition();
			beanList.forEach(e -> {
				if (e.getLongAmount() != null) {
					FuturePosition.Position longPosi = new FuturePosition.Position();
					BeanUtils.copyProperties(e, longPosi);
					futurePosition.setPosition(longPosi);
				}
				if (e.getShortAmount() != null) {
					FuturePosition.ShortPosi shortPosi = new FuturePosition.ShortPosi();
					BeanUtils.copyProperties(e, shortPosi);
					futurePosition.setShortPosi(shortPosi);
				}
			});
			return futurePosition;
		} catch (Exception e) {
			logger.error("获取期货持仓信息失败，exchangeId={}，accountId={}，coinType={}", exchangeId, accountId, contractCode, e);
			return null;
		}
	}

	/**
	 * 合约账户期末净空仓金额USD 算法：（合约账户空仓持仓数量-合约账户多仓持仓数量）* 单张合约面值
	 * 
	 * @param accountId
	 * @param exchangeId
	 * @param contractCode
	 * @return BigDecimal
	 */
	public BigDecimal getFutureUSDPosition(Long accountId, Integer exchangeId, String contractCode) {
		// FuturePosition futurePosition = getFuturePosition(accountId, exchangeId,
		// contractCode);
		BigDecimal shortAmountUSD = BigDecimal.ZERO;
		BigDecimal longAmountUSD = BigDecimal.ZERO;
		/*
		 * if (null == futurePosition.getShortPosi() || null ==
		 * futurePosition.getShortPosi().getShortAmount()) { shortAmountUSD =
		 * futurePosition.getShortPosi().getShortAmount(); } if (null ==
		 * futurePosition.getPosition() || null ==
		 * futurePosition.getPosition().getLongAmount()) { longAmountUSD =
		 * futurePosition.getPosition().getLongAmount(); }
		 */
		shortAmountUSD = test1();
		longAmountUSD = test1();

		// 单张合约面值 暂时写死
		BigDecimal totalAmountUSD = shortAmountUSD.subtract(longAmountUSD).multiply(new BigDecimal(10));
		return totalAmountUSD;
	}

	// 1~10之间随机数
	public BigDecimal test1() {
		int max = 10;
		int min = 1;
		int randomNumber = (int) Math.round(Math.random() * (max - min) + min);
		return new BigDecimal(randomNumber);
	}

}
