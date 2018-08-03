package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.common.ExchangeConfigService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanExchangeConfigMapper;
import com.huobi.quantification.dto.ExchangeConfigResqDto;
import com.huobi.quantification.entity.QuanExchangeConfig;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.xiaoleilu.hutool.util.BeanUtil;

@Service
public class ExchangeConfigServiceImpl implements ExchangeConfigService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	QuanExchangeConfigMapper quanExchangeConfigMapper;

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<List<ExchangeConfigResqDto>> getAllExchangeConfig() {
		ServiceResult<List<ExchangeConfigResqDto>> serviceResult = null;
		List<ExchangeConfigResqDto> exchangeConfigResqDtoList = new ArrayList<>();
		try {
			List<QuanExchangeConfig> list = quanExchangeConfigMapper.selectAll();
			convertToListDto(list, exchangeConfigResqDtoList);
			serviceResult = ServiceResult.buildSuccessResult(exchangeConfigResqDtoList);
		} catch (Exception e) {
			logger.error("超时异常：", e);
			serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
		}
		return serviceResult;
	}

	private void convertToListDto(List<QuanExchangeConfig> list, List<ExchangeConfigResqDto> respList) {
		for (QuanExchangeConfig temp : list) {
			ExchangeConfigResqDto exchangeConfigResqDto = new ExchangeConfigResqDto();
			BeanUtil.copyProperties(temp, exchangeConfigResqDto);
			respList.add(exchangeConfigResqDto);
		}
	}

}
