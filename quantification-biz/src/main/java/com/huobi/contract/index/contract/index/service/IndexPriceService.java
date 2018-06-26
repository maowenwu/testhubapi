package com.huobi.contract.index.contract.index.service;

import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;

import java.util.List;

public interface IndexPriceService {

    void batchSaveContractPriceIndexHis(List<ContractPriceIndexHis> hisList);

    ContractPriceIndexHis createInvalidObjFromLatestHis(ExchangeIndex exchangeIndex, Origin origin);

}
