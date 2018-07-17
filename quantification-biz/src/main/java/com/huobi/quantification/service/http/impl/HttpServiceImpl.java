package com.huobi.quantification.service.http.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.common.api.HuobiSignature;
import com.huobi.quantification.common.api.OkSignature;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.OkHttpClientUtils;
import com.huobi.quantification.common.util.ProxyConfig;
import com.huobi.quantification.dao.QuanProxyIpMapper;
import com.huobi.quantification.entity.QuanProxyIp;
import com.huobi.quantification.service.http.HttpService;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
public class HttpServiceImpl implements HttpService {

    private List<OkHttpClientUtils> clients = null;

    private AtomicInteger nextId = new AtomicInteger();

    @Autowired
    private QuanProxyIpMapper quanProxyIpMapper;

    @Autowired
    private OkSecretHolder okSecretHolder;
    
    @Autowired
    private HuobiSecretHolder huobiSecretHolder;

    private Timer timer = new Timer();
    
    public HttpServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (clients == null) {
                    return;
                }
                Iterator<OkHttpClientUtils> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    OkHttpClientUtils next = iterator.next();
                    if (next.getRequestFaildTotal() > 3) {
                        clients.remove(next);
                    }
                }
                if (clients.size() <= 0) {
                    init();
                }
            }
        }, 0, 1000);
    }

    @PostConstruct
    public void init() {
        List<QuanProxyIp> proxyIps = quanProxyIpMapper.selectAll();
        clients = new CopyOnWriteArrayList<>();
        for (QuanProxyIp proxyIp : proxyIps) {
            ProxyConfig config = new ProxyConfig();
            config.setHost(proxyIp.getHost());
            config.setPort(proxyIp.getPort());
            config.setUsername(proxyIp.getUserName());
            config.setPassword(proxyIp.getPassword());
            clients.add(OkHttpClientUtils.getInstance(config));
        }
        clients.add(OkHttpClientUtils.getInstance(null));
    }


    public OkHttpClientUtils getHttpClientUtils() {
        if (nextId.get() >= Integer.MAX_VALUE) {
            nextId = new AtomicInteger(0);
        }
        return clients.get(nextId.getAndIncrement() % clients.size());
    }

    @Override
    public String doGet(String url) throws HttpRequestException {
        return getHttpClientUtils().doGet(url, null);
    }

    @Override
    public String doGet(String url, Map<String, String> params) throws HttpRequestException {
        return getHttpClientUtils().doGet(url, params);
    }

    @Override
    public String doPost(String url, Map<String, String> params) throws HttpRequestException {
        return getHttpClientUtils().doPost(url, params);
    }

    @Override
    public String doOkSignedPost(Long accountId, String url, Map<String, String> params) throws HttpRequestException {
        OkSignature signature = okSecretHolder.getOkSignatureById(accountId);
        params = signature.sign(params);
        return getHttpClientUtils().doPost(url, params);
    }
    
    @Override
	public String doHuobiGet(Long accountId, String uri, Map<String, String> params) throws HttpRequestException {
		if (params == null) {
			params = new HashMap<>();
		}
		HuobiSignature huobiSignature = huobiSecretHolder.getHuobiSignatureById(accountId);
		return getHttpClientUtils().call(huobiSignature.getAccessKey(),huobiSignature.getSecretKey(),
				"GET", uri, null, params);
	}

	@Override
	public String doHuobiPost(Long accountId, String uri, Object object) throws HttpRequestException {
		HuobiSignature huobiSignature = huobiSecretHolder.getHuobiSignatureById(accountId);
		return getHttpClientUtils().call(huobiSignature.getAccessKey(),huobiSignature.getSecretKey(),
				"POST", uri, object, new HashMap<String, String>());
	}


}
