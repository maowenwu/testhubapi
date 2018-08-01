package com.huobi.quantification.service;

import com.huobi.quantification.entity.StrategyOrderConfig;

public interface ConfigService {


    StrategyOrderConfig selectByPrimaryKey(Integer id);
}
