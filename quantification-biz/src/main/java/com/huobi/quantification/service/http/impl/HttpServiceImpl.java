package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.api.OkSignature;
import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.HttpClientUtils;
import com.huobi.quantification.common.util.MD5;
import com.huobi.quantification.common.util.ProxyConfig;
import com.huobi.quantification.service.http.HttpService;
import org.apache.commons.collections.MapUtils;
import org.apache.http.params.HttpParams;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
public class HttpServiceImpl implements HttpService {

    private HttpClientUtils httpClientUtils = null;

    public HttpServiceImpl() {
        ProxyConfig config = new ProxyConfig();
        config.setHost("proxy.huobidev.com");
        config.setPort(3129);
        httpClientUtils = HttpClientUtils.getInstance(config);
    }

    @Override
    public String doGet(String url) throws HttpRequestException {
        return httpClientUtils.doGet(url);
    }

    @Override
    public String doGet(String url, Map<String, String> params) throws HttpRequestException {
        return httpClientUtils.doGet(url, params);
    }

    @Override
    public String doPost(String url, Map<String, String> params) throws HttpRequestException {
        return httpClientUtils.doPost(url, params);
    }

    @Override
    public String okSignedPost(String url, Map<String, String> params) throws HttpRequestException {
        params = new OkSignature().sign(params);
        return httpClientUtils.doPost(url, params);
    }

}
