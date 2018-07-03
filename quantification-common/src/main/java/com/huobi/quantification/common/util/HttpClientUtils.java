package com.huobi.quantification.common.util;

import com.huobi.quantification.common.exception.HttpRequestException;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    private CloseableHttpClient httpClient;

    private HttpClientUtils(ProxyConfig proxyConfig) {
        HttpClientBuilder builder = HttpClients.custom()
                .setSSLSocketFactory(getSslFactory())
                .setDefaultRequestConfig(defaultRequestConfig())
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(45);
        if (proxyConfig == null) {
            httpClient = builder.build();
        } else {
            HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(Optional.ofNullable(proxyConfig.getUsername()).orElse("")
                    , Optional.ofNullable(proxyConfig.getPassword()).orElse("")));
            httpClient = builder.setRoutePlanner(routePlanner)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
        }

    }

    private static RequestConfig defaultRequestConfig() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        return defaultRequestConfig;
    }

    private static LayeredConnectionSocketFactory getSslFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return new SSLConnectionSocketFactory(ctx);
    }

    public static HttpClientUtils getInstance(ProxyConfig proxyConfig) {
        return new HttpClientUtils(proxyConfig);
    }

    public String doGet(String url) {
        return doGet(url, null);
    }

    public String doGet(String url, Map<String, String> params) {
        URIBuilder builder = null;
        URI uri = null;
        try {
            builder = new URIBuilder(url);
            if (MapUtils.isNotEmpty(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = builder.build();
        } catch (URISyntaxException e) {
            throw new HttpRequestException("url地址解析异常", e);
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(defaultRequestConfig());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        httpGet.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new HttpRequestException("http执行异常，url=" + url, e);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        }
        throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + url);
    }

    public String doPost(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultRequestConfig());
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
        httpPost.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> valuePairs = new ArrayList<>();
            params.forEach((k, v) -> {
                valuePairs.add(new BasicNameValuePair(k, v));
            });
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new HttpRequestException("http请求参数编码异常", e);
            }
            httpPost.setEntity(entity);
        }

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new HttpRequestException("http执行异常，url=" + url, e);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                throw new HttpRequestException("http结果解析异常", e);
            }
        }
        throw new HttpRequestException("响应码不为200，返回响应码：" + statusCode + "，url：" + url);

    }
}
