package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.ExchangeIpPool;
import com.huobi.contract.index.entity.IpUsageRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IpUsageRecordMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(IpUsageRecord record);

    int insertSelective(IpUsageRecord record);

    IpUsageRecord selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(IpUsageRecord record);

    int updateByPrimaryKey(IpUsageRecord record);

    /**
     * 查询最新被禁用的交易所-IP
     */
    List<ExchangeIpPool> listLatestUnfreezeIp();

    /**
     * 根据exchangeId,ipaddr获取最近使用的记录
     * @param exchangeId
     * @param ipAddr
     * @return
     */
    IpUsageRecord getLatestUseRecordByExchangeIdIp(@Param("exchangeId") long exchangeId, @Param("ipAddr") String ipAddr);
}