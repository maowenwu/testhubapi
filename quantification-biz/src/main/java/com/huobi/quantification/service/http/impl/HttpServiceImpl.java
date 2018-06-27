package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.common.util.HttpClientUtils;
import com.huobi.quantification.common.util.MD5;
import com.huobi.quantification.common.util.ProxyConfig;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public String doPost(String url, Map<String, String> params) throws HttpRequestException {
        return httpClientUtils.doPost(url, params);
    }

    @Override
    public String okSignedPost(String url, Map<String, String> params) throws HttpRequestException {
        params.put("api_key", "d880067d-65b7-4bcb-b366-4cea186001a7");
        params.put("sign", getSign(params));
        return httpClientUtils.doPost(url, params);
    }

    private String getSign(Map<String, String> params) {
        List<String> sortParam = params.entrySet().stream()
                .map((e) -> e.getKey() + "=" + e.getValue())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        sortParam.add("secret_key=F6D5CF92E40A0FA178BDF05FB9801BED");
        String preSign = String.join("&", sortParam);
        return MD5.hash(preSign);
    }
}
