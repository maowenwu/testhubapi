package com.huobi.contract.index.dao;

import com.huobi.contract.index.dto.ExchangeIpPool;
import com.huobi.contract.index.entity.IpPool;

import java.util.List;

public interface IpPoolMapper {
    int deleteByPrimaryKey(Long seqId);

    int insert(IpPool record);

    int insertSelective(IpPool record);

    IpPool selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(IpPool record);

    int updateByPrimaryKey(IpPool record);

    /**
     * 查询各交易所可以使用的IP
     * @return
     */
    List<ExchangeIpPool> listExchangeIp();
}