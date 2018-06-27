package com.huobi.quantification.service.http.impl;

import com.huobi.quantification.common.exception.HttpRequestException;
import com.huobi.quantification.service.http.HttpService;
import org.springframework.stereotype.Service;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
public class HttpServiceImpl implements HttpService {

    @Override
    public String doGet(String url) throws HttpRequestException {
        return null;
    }
}
