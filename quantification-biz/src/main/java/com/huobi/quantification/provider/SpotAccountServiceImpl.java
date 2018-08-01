package com.huobi.quantification.provider;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.account.HuobiAccountService;
import com.huobi.quantification.service.redis.RedisService;

@Service
public class SpotAccountServiceImpl implements SpotAccountService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private HuobiAccountService huobiAccountService;

	@Override
	public ServiceResult<SpotBalanceRespDto> getBalance(SpotBalanceReqDto balanceReqDto) {
		ServiceResult<SpotBalanceRespDto> serviceResult = null;
		try {
			SpotBalanceRespDto balanceRespDto = AsyncUtils.supplyAsync(() -> {
				while (!Thread.interrupted()) {
					// 从redis读取最新用户资产
					List<QuanAccountAsset> assets = redisService.getSpotUserInfo(balanceReqDto.getAccountId(),
							balanceReqDto.getExchangeId());
					if (assets == null) {
						ThreadUtils.sleep10();
						continue;
					}
					Date ts = assets.get(0).getTs();
					logger.info("QuanAccountSpotAsset时间：{}",DateUtils.format(ts, "yyyy-MM-dd HH:mm:ss"));
					logger.info("当前时间：{}",DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					if (DateUtils.withinMaxDelay(ts, balanceReqDto.getMaxDelay())) {
						return parseBalanceResp(balanceReqDto.getExchangeId(), assets);
					} else {
						ThreadUtils.sleep10();
						continue;
					}
				}
				return null;
			}, balanceReqDto.getTimeout());
			serviceResult = ServiceResult.buildSuccessResult(balanceRespDto);
		} catch (ExecutionException e) {
			logger.error("执行异常：", e);
			serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
		} catch (TimeoutException e) {
			logger.error("超时异常：", e);
			serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
		}
		return serviceResult;
	}

	private SpotBalanceRespDto parseBalanceResp(int exchangeId, List<QuanAccountAsset> assets) {
		if (exchangeId == ExchangeEnum.HUOBI.getExId()) {
			return parseHuobiSpotBalanceResp(assets);
		}
		throw new UnsupportedOperationException("不支持该交易所" + exchangeId);
	}

	private SpotBalanceRespDto parseHuobiSpotBalanceResp(List<QuanAccountAsset> assets) {
		SpotBalanceRespDto respDto = new SpotBalanceRespDto();
		respDto.setTs(assets.get(0).getTs());
		Map<String, SpotBalanceRespDto.DataBean> dataBean = new HashMap<>();
		for (QuanAccountAsset quanAccountAsset : assets) {
			dataBean.put(quanAccountAsset.getCoin(), convertToDto(quanAccountAsset));
		}
		respDto.setData(dataBean);
		return respDto;
	}

	private SpotBalanceRespDto.DataBean convertToDto(QuanAccountAsset quanAccountAsset) {
		SpotBalanceRespDto.DataBean respDto = new SpotBalanceRespDto.DataBean();
		respDto.setAvailable(quanAccountAsset.getAvailable());
		respDto.setFrozen(quanAccountAsset.getFrozen());
		respDto.setTotal(quanAccountAsset.getTotal());
		return respDto;
	}

	@Override
	public void saveFirstBalance(long accountId, String contractCode) {
		List<QuanAccountAsset> account = huobiAccountService.getAccount(accountId);
		redisService.saveFirstSpotAccounts(account, accountId, contractCode);
	}

	@Override
	public List<QuanAccountAsset> getFirstBalance(long accountId, String contractCode) {
		return redisService.getFirstSpotAccounts(accountId, contractCode);
	}

	@Override
	public void saveFirstDebit(Map<String, BigDecimal> hashMap) {
		redisService.saveFirstDebit(hashMap);
	}

	@Override
	public Map<String, BigDecimal> getFirstDebit() {
		return redisService.getFirstDebit();
	}

}
