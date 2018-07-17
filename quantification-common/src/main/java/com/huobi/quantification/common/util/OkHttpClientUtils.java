package com.huobi.quantification.common.util;

import com.huobi.quantification.common.exception.ApiException;
import com.huobi.quantification.common.exception.HttpRequestException;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class OkHttpClientUtils {

    private OkHttpClient httpClient;

    private AtomicInteger numRequestFaild = new AtomicInteger(0);
    
	static final MediaType JSON = MediaType.parse("application/json");

    private OkHttpClientUtils(ProxyConfig proxyConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS);
        if (proxyConfig == null) {
            httpClient = builder.build();
        } else {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.huobidev.com", 3129)));
            if (proxyConfig.getUsername() != null && proxyConfig.getPassword() != null) {
                builder.authenticator((route, response) -> {
                    String credential = Credentials.basic(proxyConfig.getUsername(), proxyConfig.getPassword());
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                });
            }
            httpClient = builder.build();
        }

    }

    public static OkHttpClientUtils getInstance(ProxyConfig proxyConfig) {
        return new OkHttpClientUtils(proxyConfig);
    }

    public String doGet(String url, Map<String, String> params) {
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                urlBuilder.addQueryParameter(k, v);
            });
        }
        reqBuild.url(urlBuilder.build());

        Response response = null;
        try {
            response = httpClient.newCall(reqBuild.build()).execute();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                numRequestFaild.getAndIncrement();
            }
            throw new HttpRequestException("http执行异常", e);
        }
        if (response.isSuccessful()) {
            try {
                reset();
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + urlBuilder.build());
        }
    }

    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                builder.add(k, v);
            });
        }

        Request.Builder reqBuilder = new Request.Builder().url(url);
        if (MapUtils.isNotEmpty(params)) {
            reqBuilder.post(builder.build());
        }

        Response response = null;
        try {
            response = httpClient.newCall(reqBuilder.build()).execute();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                numRequestFaild.getAndIncrement();
            }
            throw new HttpRequestException("http执行异常", e);
        }
        if (response.isSuccessful()) {
            try {
                reset();
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + reqBuilder.build());
        }
    }
    
    public String call(String accessKeyId, String accessKeySecret,String method, String uri, Object object, Map<String, String> params) {
        ApiSignature sign = new ApiSignature();
        sign.createSignature(accessKeyId, accessKeySecret, method, uri, params);
        try {
            Request.Builder builder = null;
            if ("POST".equals(method)) {
                RequestBody body = RequestBody.create(JSON, JsonUtil.writeValue(object));
                builder = new Request.Builder().url(uri + "?" + toQueryString(params)).post(body);
            } else {
                builder = new Request.Builder().url(uri + "?" + toQueryString(params)).get();
            }
            Request request = builder.build();
            Response response = httpClient.newCall(request).execute();
            String s = response.body().string();
            return s;
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    private String toQueryString(Map<String, String> params) {
        return String.join("&", params.entrySet().stream().map((entry) -> {
            return entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue());
        }).collect(Collectors.toList()));
    }

    private void reset() {
        numRequestFaild.set(0);
    }

    public int getRequestFaildTotal() {
        return numRequestFaild.get();
    }
}
