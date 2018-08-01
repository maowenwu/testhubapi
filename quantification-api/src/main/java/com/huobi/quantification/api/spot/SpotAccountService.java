package com.huobi.quantification.api.spot;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.entity.QuanAccountAsset;

public interface SpotAccountService {

	/**
     * 获取账户余额(权益)
     *
     * @param balanceReqDto
     * @return
     */
    ServiceResult<SpotBalanceRespDto> getBalance(SpotBalanceReqDto balanceReqDto);

	void saveFirstBalance(long accountId, Integer exchangeId);
	
	List<QuanAccountAsset> getFirstBalance(long accountId, Integer exchangeId);

	void saveFirstDebit(Map<String, BigDecimal> hashMap, String contractCode);
	
	Map<String, BigDecimal> getFirstDebit(String contractCode);
}
