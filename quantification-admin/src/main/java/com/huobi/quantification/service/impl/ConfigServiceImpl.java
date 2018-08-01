package com.huobi.quantification.service.impl;

import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private StrategyOrderConfigMapper strategyOrderConfigMapper;

    @Override
    public StrategyOrderConfig selectByPrimaryKey(Integer id) {
        return strategyOrderConfigMapper.selectByPrimaryKey(id);
    }
}
