package com.huobi.contract.index.contract.index.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.contract.index.contract.index.service.HuobiApiService;
import com.huobi.quantification.index.entity.QuantificationTicker;;
@Service("huobiApiService")
public class HuobiApiServiceImpl implements HuobiApiService {

    @Autowired
   // private QuantificationTickerMapper quantificationTickerMappeOr;
	
    public void  qntificationTickerMapperinsert() {
    	QuantificationTicker record=new QuantificationTicker();
    	record.setId(35l);
    	//quantificationTickerMapper.insert(record);
    }
}