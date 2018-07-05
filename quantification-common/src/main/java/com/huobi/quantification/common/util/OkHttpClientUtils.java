package com.huobi.quantification.common.util;

import com.huobi.quantification.common.exception.HttpRequestException;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class OkHttpClientUtils {

    private OkHttpClient httpClient;

    private OkHttpClientUtils(ProxyConfig proxyConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS);
        if (proxyConfig == null) {
            httpClient = builder.build();
        } else {
            httpClient = builder
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.huobidev.com", 3129)))
                    .build();
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
            throw new HttpRequestException("http执行异常", e);
        }
        if (response.isSuccessful()) {
            try {
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
            throw new HttpRequestException("http执行异常", e);
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        } else {
            int statusCode = response.code();
            throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + reqBuilder.build());
        }
    }
}
