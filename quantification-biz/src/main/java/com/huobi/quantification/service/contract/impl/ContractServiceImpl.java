package com.huobi.quantification.service.contract.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanContractCodeMapper;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.response.future.HuobiFutureContractCodeResponse;
import com.huobi.quantification.service.contract.ContractService;
import com.huobi.quantification.service.http.HttpService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private QuanContractCodeMapper contractCodeMapper;

    @Autowired
    private HttpService httpService;

    @Override
    public void updateHuobiContractCode() {
        Stopwatch started = Stopwatch.createStarted();
        logger.info("[HuobiContractCode]任务开始");
        List<QuanContractCode> contractCodeList = contractCodeMapper.selectByExchangeId(ExchangeEnum.HUOBI_FUTURE.getExId());
        for (QuanContractCode contractCode : contractCodeList) {
            String code = queryHuobiContractCodeByAPI(contractCode.getSymbol().toUpperCase(), contractCode.getContractType());
            if (StringUtils.isNotEmpty(code)) {
                contractCode.setContractCode(code);
                contractCode.setUpdateTime(new Date());
                contractCodeMapper.updateByPrimaryKey(contractCode);
            }
        }
        logger.info("[HuobiContractCode]任务结束，耗时：" + started);
    }

    private String queryHuobiContractCodeByAPI(String symbol, String contractType) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("contract_type", contractType);
        String body = httpService.doGet(HttpConstant.HUOBI_CONTRACE_CODE, params);
        HuobiFutureContractCodeResponse response = JSON.parseObject(body, HuobiFutureContractCodeResponse.class);
        if ("ok".equalsIgnoreCase(response.getStatus())) {
            List<HuobiFutureContractCodeResponse.DataBean> data = response.getData();
            if (CollectionUtils.isNotEmpty(data)) {
                return data.get(0).getContractCode();
            }
        }
        return null;
    }

    @Override
    public QuanContractCode getContractCode(int exchangeId, String symbol, String contractType) {
        return contractCodeMapper.selectContractCode(exchangeId, symbol, contractType);
    }

    @Override
    public QuanContractCode getContractCode(int exchangeId, String contractCode) {
        return contractCodeMapper.selectContractCodeByCode(exchangeId, contractCode);
    }
}
