package com.huobi.quantification.provider;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ContractCodeDto;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.response.future.ExchangeRateResponse;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FutureContractServiceImpl implements FutureContractService {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContractService contractService;

    @Autowired
    private HttpService httpService;

    @Override
    public ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String symbol, String contractType) {
        ContractCodeDto contractCodeDto = new ContractCodeDto();
        QuanContractCode contractCode = contractService.getContractCode(exchangeId, symbol, contractType);
        BeanUtils.copyProperties(contractCode, contractCodeDto);
        return ServiceResult.buildSuccessResult(contractCodeDto);
    }

    @Override
    public ServiceResult<BigDecimal> getExchangeRateOfUSDT2USD() {
        try {
            String body = httpService.doGet("https://api.coinmarketcap.com/v2/ticker/825/");
            ExchangeRateResponse response = JSON.parseObject(body, ExchangeRateResponse.class);
            BigDecimal price = response.getData().getQuotes().getUSD().getPrice();
            return ServiceResult.buildSuccessResult(price);
        } catch (Throwable e) {
            logger.error("获取USDT转USD汇率异常", e);
            return ServiceResult.buildErrorResult(ServiceErrorEnum.HTTP_REQUEST_ERROR);
        }
    }
}
