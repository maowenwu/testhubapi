package com.huobi.contract.index.contract.index.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.RestTemplateUtil;
import com.huobi.contract.index.contract.index.common.CoinStation;
import com.huobi.contract.index.contract.index.common.CurrencyEnum;
import com.huobi.contract.index.contract.index.service.IndexGrabService;
import com.huobi.contract.index.dao.ExchangeRateHisMapper;
import com.huobi.contract.index.dto.CurrencylayerExchangeRateResp;
import com.huobi.contract.index.entity.ExchangeRateHis;
import com.huobi.contract.index.entity.ValidEnum;
import com.okcoin.rest.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("exchangeRateGrabService")
public class ExchangeRateGrabServiceImpl extends BaseIndexGrabService implements IndexGrabService {
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateGrabServiceImpl.class);
    @Autowired
    private ExchangeRateHisMapper exchangeRateHisMapper;
    @Value("${exchange_grap_retry_count}")
    private Integer exchangeGrapRetryCount;

    private static final Integer ONERETRYSLEEP = 1000 * 60 * 3;

    private static final Integer TWORETRYSLEEP = 1000 * 60 * 5;

    @Override
    public void grab() {
        List<String> listCurrency = CurrencyEnum.listShortNames();
        //抓取以美元计价其他货币 eg:XXX-USD
        //grabUseUSDPricingOther(listCurrency);
        //抓取以其他货币计价美元 eg:USD-XXX
        grabUseOtherPricingUSD(listCurrency);
    }

    /**
     * 抓取以美元计价其他货币 eg:XXX-USD
     */
    public void grabUseUSDPricingOther(List<String> listCurrency) {
        List<ExchangeRateHis> list = Lists.newArrayList();
        Date now = new Date();
        for (String currency : listCurrency) {
            String rateSymbol = currency + Constant.USD_SHORTNAME;
            try {
                String result = retryGrab(CoinStation.CURRENCYSOURCE.getName());
                if(StringUtil.isEmpty(result)){
                    batchInsertFailExchangeRateHis(rateSymbol, now);
                    continue;
                }
                CurrencylayerExchangeRateResp currencylayerExchangeRateResp = JSONObject.parseObject(result,
                        CurrencylayerExchangeRateResp.class);
                if (currencylayerExchangeRateResp.getSuccess() && null != currencylayerExchangeRateResp.getQuotes()) {
                    JSONObject object = currencylayerExchangeRateResp.getQuotes();
                    Iterator iterator = object.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        if (entry.getKey() == null) {
                            continue;
                        }
                        if (entry.getKey().toString().contains(Constant.USD_SHORTNAME)) {
                            BigDecimal value = new BigDecimal(entry.getValue().toString());
                            list.add(new ExchangeRateHis(Constant.DEFAULT_EXCHANGEID, rateSymbol, value, now, now,ValidEnum.SUCC.getStatus()));
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(currency + "汇率抓取失败", e);
                batchInsertFailExchangeRateHis(rateSymbol, now);
            }
        }
        super.batchInsertSuccExchangeRateHis(list);
    }

    /**
     * 抓取以其他货币计价美元 eg:USD-XXX
     */
    public void grabUseOtherPricingUSD(List<String> listCurrency) {
        Date now = new Date();
        //轮询抓取
        String result = retryGrab(CoinStation.CURRENCYLAYER.getName());
        if (StringUtils.isBlank(result)) {
            batchInsertFailExchangeRateHis(listCurrency, now);
            return;
        }
        List<ExchangeRateHis> list = null;
        try {
            list = Lists.newArrayList();
            CurrencylayerExchangeRateResp currencylayerExchangeRateResp = JSONObject.parseObject(result,
                    CurrencylayerExchangeRateResp.class);
            if (currencylayerExchangeRateResp.getSuccess() && null != currencylayerExchangeRateResp.getQuotes()) {
                JSONObject object = currencylayerExchangeRateResp.getQuotes();
                Iterator iterator = object.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String rateSymbol = getRateSymbol(entry.getKey().toString());
                    String currencyName = entry.getKey().toString().replace(Constant.USD_SHORTNAME, "");
                    if (CurrencyEnum.isContainShortName(currencyName)) {
                        BigDecimal value = new BigDecimal(entry.getValue().toString());
                        list.add(new ExchangeRateHis(Constant.DEFAULT_EXCHANGEID, rateSymbol, value, now, now,ValidEnum.SUCC.getStatus()));
                        listCurrency.remove(currencyName);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(CoinStation.CURRENCYLAYER.getUrl() + "获取失败", e);
        }
        super.batchInsertSuccExchangeRateHis(list);
        super.batchInsertFailExchangeRateHis(listCurrency, now);
    }

    /**
     * USD-XXX 汇率重试抓取
     * 1.第一次重试 休眠3min
     * 2.第二次重试休眠5min
     * @return
     */
    public String retryGrab(String currencyName){
        int count = 1;
        String result = requestResult(currencyName);
        if(StringUtil.isEmpty(result)){
            while(count<=Constant.RATE_GRAB_RETRY_COUNT){
                LOG.info("汇率抓取第["+count+"]次重试");
                try {
                    if(count == 1){
                        Thread.sleep(ONERETRYSLEEP);
                    }
                    if(count == 2){
                        Thread.sleep(TWORETRYSLEEP);
                    }
                } catch (InterruptedException e) {
                    LOG.error("汇率抓取重试睡眠异常", e);
                    Thread.currentThread().interrupt();
                }
                count++;
                result = requestResult(currencyName);
                if(!StringUtil.isEmpty(result)){
                    break;
                }
            }
        }
        return result;
    }
    /**
     * 汇率抓取请求
     * 轮询URL抓取
     * @return
     */
    public String requestResult(String currencyName){
        String result = null;
        List<String> urlList = CoinStation.getAllCurrencyLayer(currencyName);
        for(String url:urlList){
            try {
                result = RestTemplateUtil.getInstance(null).get(url);
            } catch (Exception e) {
                LOG.error(url + "汇率获取失败", e);
                continue;
            }
            if(StringUtil.isEmpty(result)){
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(jsonObject!=null && jsonObject.get("success").toString().equals("false")){
                continue;
            }
            return result;
        }
        return result;
    }
    /**
     * @param key
     * @return
     */
    private String getRateSymbol(String key) {
        StringBuffer sb = null;
        //判断是否已USD开头
        if (key.indexOf(Constant.USD_SHORTNAME) == 0) {
            String currencyName = key.replace(Constant.USD_SHORTNAME, "");
            sb = new StringBuffer(Constant.USD_SHORTNAME);
            sb.append("-");
            sb.append(currencyName);

        } else {
            String currencyName = key.replace(Constant.USD_SHORTNAME, "");
            sb = new StringBuffer(currencyName);
            sb.append("-");
            sb.append(Constant.USD_SHORTNAME);
        }
        return sb.toString();
    }
}
