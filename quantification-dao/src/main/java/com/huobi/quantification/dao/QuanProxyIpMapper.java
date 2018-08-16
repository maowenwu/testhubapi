package com.huobi.quantification.dao;

import com.huobi.quantification.entity.QuanProxyIp;
import java.util.List;

public interface QuanProxyIpMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuanProxyIp record);

    QuanProxyIp selectByPrimaryKey(Long id);

    List<QuanProxyIp> selectAll();

    int updateByPrimaryKey(QuanProxyIp record);

    List<QuanProxyIp> selectList(QuanProxyIp entity);
}