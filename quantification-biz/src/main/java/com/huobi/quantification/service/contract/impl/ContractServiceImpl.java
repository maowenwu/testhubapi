package com.huobi.quantification.service.contract.impl;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanContractCodeMapper;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.future.OKFutureContractCodeResponse;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.http.HttpService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private QuanContractCodeMapper contractCodeMapper;

    @Autowired
    private HttpService httpService;

    @Override
    public void updateOkContractCode() {
        List<QuanContractCode> contractCodeList = contractCodeMapper.selectByExchangeId(ExchangeEnum.OKEX.getExId());
        for (QuanContractCode contractCode : contractCodeList) {
            String code = queryOkContractCodeByAPI(contractCode.getSymbol(), contractCode.getContractType());
            if (StringUtils.isNotEmpty(code)) {
                contractCode.setContractCode(code);
                contractCode.setUpdateTime(new Date());
                contractCodeMapper.updateByPrimaryKey(contractCode);
            }
        }
    }

    private String queryOkContractCodeByAPI(String symbol, String contractType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contractType", contractType);
        String body = httpService.doGet(HttpConstant.OK_CONTRACE_CODE, params);
        List<OKFutureContractCodeResponse> codeResponses = JSON.parseArray(body, OKFutureContractCodeResponse.class);
        if (CollectionUtils.isNotEmpty(codeResponses)) {
            return codeResponses.get(0).getContractCode();
        } else {
            return null;
        }
    }

    @Override
    public QuanContractCode getContractCode(int exchangeId, String symbol, String contractType) {
        return contractCodeMapper.selectContractCode(exchangeId, symbol, contractType);
    }

    @Override
    public QuanContractCode getContractCode(int exchangeId, String contractCode) {
        return contractCodeMapper.selectContractCodeByCode(exchangeId,contractCode);
    }
}
