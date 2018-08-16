package cn.huobi.framework.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.QuanProxyIp;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.ProxyIp;

public interface QuanProxyIpService {

    List<ProxyIp> selectByCondition(ProxyIp proxy, Page<ProxyIp> page);

    int update(ProxyIp proxy, Boolean switchValue);

    int insert(QuanProxyIp proxy);

    int deleteById(Integer id);

    PageInfo<QuanProxyIp> selectPage(QuanProxyIp proxy,PageInfo<QuanProxyIp> pageInfo);

}
