package com.huobi.quantification.service.contract;

import com.huobi.quantification.entity.QuanContractCode;

public interface ContractService {


    void updateOkContractCode();


    QuanContractCode getContractCode(int exchangeId, String symbol, String contractType);

    QuanContractCode getContractCode(int exchangeId, String contractCode);
}
