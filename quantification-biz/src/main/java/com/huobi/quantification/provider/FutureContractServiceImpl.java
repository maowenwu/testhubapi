package com.huobi.quantification.provider;

import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.ContractCodeDto;
import com.huobi.quantification.entity.QuanContractCode;
import com.huobi.quantification.service.contract.ContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FutureContractServiceImpl implements FutureContractService {


    @Autowired
    private ContractService contractService;

    @Override
    public ServiceResult<ContractCodeDto> getContractCode(int exchangeId, String symbol, String contractType) {
        ContractCodeDto contractCodeDto = new ContractCodeDto();
        QuanContractCode contractCode = contractService.getContractCode(exchangeId, symbol, contractType);
        BeanUtils.copyProperties(contractCode, contractCodeDto);
        return ServiceResult.buildSuccessResult(contractCodeDto);
    }
}
