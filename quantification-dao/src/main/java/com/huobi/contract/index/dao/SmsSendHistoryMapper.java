package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.SmsSendHistory;

public interface SmsSendHistoryMapper {
    int insert(SmsSendHistory record);

    int insertSelective(SmsSendHistory record);
}