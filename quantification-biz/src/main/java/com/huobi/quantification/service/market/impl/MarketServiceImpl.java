package com.huobi.quantification.service.market.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangl
 * @since 2018/6/26
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {

    @Autowired
    private HttpService httpService;

    @Override
    public Object getOkTicker(String symbol, String contractType) {
        String url = HttpConstant.OK_TICKER + "?symbol=" + symbol + "&contract_type=" + contractType;
        //String body = httpService.doGet(url);
        String body="{\"date\":\"1530075027\",\"ticker\":{\"high\":6269.01,\"vol\":3066748,\"day_high\":0,\"last\":6054.85,\"low\":6018.61,\"contract_id\":201806290000034,\"buy\":6049.73,\"sell\":6053.71,\"coin_vol\":0,\"day_low\":0,\"unit_amount\":100}}";
        JSONObject jsonObject = JSON.parseObject(body);
        return null;
    }

    @Override
    public Object getOkDepth(String symbol, String contractType) {
        String url = HttpConstant.OK_DEPTH + "?symbol=" + symbol + "&contract_type=" + contractType;
        //{"asks":[[6055.95,100],[6055,75],[6053.88,26],[6053.71,22],[6052.86,80]],"bids":[[6048.31,100],[6048.2,35],[6047.5,100],[6047.24,100],[6047.11,80]]}
        return httpService.doGet(url);
    }

}
