package com.huobi.contract.index.dao;

import com.huobi.contract.index.entity.EmailSendHistory;

public interface EmailSendHistoryMapper {
    int deleteByPrimaryKey(Long emailId);

    int insert(EmailSendHistory record);

    int insertSelective(EmailSendHistory record);

    EmailSendHistory selectByPrimaryKey(Long emailId);

    int updateByPrimaryKeySelective(EmailSendHistory record);

    int updateByPrimaryKeyWithBLOBs(EmailSendHistory record);

    int updateByPrimaryKey(EmailSendHistory record);
}