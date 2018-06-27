package com.huobi.quantification.service.http;

import com.huobi.quantification.common.exception.HttpRequestException;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface HttpService {

    String doGet(String url) throws HttpRequestException;
}
