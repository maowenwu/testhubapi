package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.ProxyDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.ProxyIp;
import cn.huobi.framework.service.QuanProxyIpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.QuanProxyIpMapper;
import com.huobi.quantification.entity.QuanProxyIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("quanProxyIpService")
public class QuanProxyIpServiceImpl implements QuanProxyIpService {

    @Resource
    private ProxyDao proxyDao;

    @Autowired
    QuanProxyIpMapper quanProxyIpMapper;

    @Override
    public List<ProxyIp> selectByCondition(ProxyIp proxy, Page<ProxyIp> page) {
        QuanProxyIp quanProxyIp = convertProxyIp(proxy);
        List<QuanProxyIp> ips = proxyDao.selectByCondition(quanProxyIp, page);
        List<ProxyIp> proxyIps = new ArrayList<>();
        for (QuanProxyIp quanProxyIp2 : ips) {
            ProxyIp proxyIp = converQuanProxyIp(quanProxyIp2);
            proxyIps.add(proxyIp);
        }
        return proxyIps;
    }

    private ProxyIp converQuanProxyIp(QuanProxyIp quanProxyIp2) {
        ProxyIp proxyIp = new ProxyIp();
        proxyIp.setId(quanProxyIp2.getId());
        proxyIp.setHost(quanProxyIp2.getHost());
        proxyIp.setPassword(quanProxyIp2.getPassword());
        proxyIp.setPort(quanProxyIp2.getPort());
        proxyIp.setUserName(quanProxyIp2.getUserName());
        if (quanProxyIp2.getState() > 0) {
            proxyIp.setState("开启");
        } else {
            proxyIp.setState("关闭");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        proxyIp.setCreateTime(formatter.format(quanProxyIp2.getCreateTime()));
        proxyIp.setUpdateTime(formatter.format(quanProxyIp2.getUpdateTime()));
        return proxyIp;
    }

    private QuanProxyIp convertProxyIp(ProxyIp proxy) {
        QuanProxyIp quanProxyIp = new QuanProxyIp();
        quanProxyIp.setHost(proxy.getHost());
        quanProxyIp.setPort(proxy.getPort());
        quanProxyIp.setUserName(proxy.getUserName());
        quanProxyIp.setPassword(proxy.getPassword());
        quanProxyIp.setId(proxy.getId());
        quanProxyIp.setUpdateTime(new Date());
        return quanProxyIp;
    }

    @Override
    public int update(ProxyIp proxy, Boolean switchValue) {
        QuanProxyIp convertProxyIp = convertProxyIp(proxy);
        if (switchValue == null || !switchValue) {
            convertProxyIp.setState(0);
        } else {
            convertProxyIp.setState(1);
        }
        return proxyDao.update(convertProxyIp);
    }

    @Override
    public int insert(QuanProxyIp proxy) {
        return proxyDao.insert(proxy);
    }

    @Override
    public int deleteById(Integer id) {
        return proxyDao.delete(id);
    }

    @Override
    public PageInfo<QuanProxyIp> selectPage(QuanProxyIp entity,PageInfo<QuanProxyIp> pageInfo) {
        List<QuanProxyIp> list = quanProxyIpMapper.selectList(entity);
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageInfo = new PageInfo<>(list);
        return pageInfo;
    };

}
