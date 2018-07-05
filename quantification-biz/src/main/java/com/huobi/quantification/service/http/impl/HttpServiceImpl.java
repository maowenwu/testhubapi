package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.api.OkSignature;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.OkHttpClientUtils;
import com.huobi.quantification.common.util.ProxyConfig;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
public class HttpServiceImpl implements HttpService {

    private OkHttpClientUtils[] clients = new OkHttpClientUtils[1];

    private AtomicInteger nextId = new AtomicInteger();

    @Autowired
    private OkSecretHolder okSecretHolder;

    public HttpServiceImpl() {
        ProxyConfig config = new ProxyConfig();
        config.setHost("proxy.huobidev.com");
        config.setPort(3129);
        clients[0] = OkHttpClientUtils.getInstance(config);

        /*config.setHost("172.31.6.86");
        config.setPort(13128);
        clients[0] = HttpClientUtils.getInstance(config);


        config.setHost("54.248.65.254");
        config.setPort(13129);
        clients[1] = HttpClientUtils.getInstance(config);

        config.setHost("13.230.239.28");
        config.setPort(13129);
        clients[2] = HttpClientUtils.getInstance(config);*/
    }

    public OkHttpClientUtils getHttpClientUtils() {
        if (nextId.get() >= Integer.MAX_VALUE) {
            nextId = new AtomicInteger(0);
        }
        return clients[nextId.getAndIncrement() % clients.length];
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

}
