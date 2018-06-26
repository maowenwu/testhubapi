package com.huobi.contract.index.api;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.huobi.contract.index.api.LastestTickerNotifyService;
import com.huobi.contract.index.common.mail.EmailSend;
import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.dao.ContractPriceIndexOkexMapper;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ContractPriceIndexOkex;
import com.huobi.contract.index.entity.IndexInfo;
import com.huobi.contract.index.okcoin.FutureClientFacade;
import com.huobi.contract.index.taskjob.LastestTickerNotifyJob;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("lastestTickerNotifyService")
public class LastestTickerNotifyServciceImpl implements LastestTickerNotifyService {
    private static final Logger LOG = LoggerFactory.getLogger(LastestTickerNotifyJob.class);

    @Value("${notify.ticker.mail.username}")
    private String notifyMailUserName = "";

    private static final String SYMBOL_BTC_USDT = "btc_usdt";
    @Autowired
    private EmailSend emailSend;
    @Value("${notify.ticker.interval.usdt}")
    private Integer usdtInterval;
    @Autowired
    private RedisService redisService;
    @Autowired
    private FutureClientFacade futureClientFacade;
    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Autowired
    private ContractPriceIndexOkexMapper contractPriceIndexOkexMapper;
    @Override
    public void calc() {
        try {
            String index = futureClientFacade.future_index(SYMBOL_BTC_USDT);
            String rate = futureClientFacade.exchange_rate();
            LOG.debug("index={},rate={}", index,rate);
            if (StringUtils.isNotEmpty(index)) {
                //TickerResp tickerResp = JSONObject.parseObject(tickerJson, TickerResp.class);
                //if (StringUtils.isNotEmpty(rate)) {
                //	tickerResp.getTicker().setRate(new BigDecimal(rate));
                //}
                String oldBtcUsdtStr = redisService.get(SYMBOL_BTC_USDT);
                LOG.debug("oldBtcUsdtStr={}", oldBtcUsdtStr);
                BigDecimal oldBtcUsdt = null;
                BigDecimal btcUsdt = new BigDecimal(index);
                if (StringUtils.isBlank(oldBtcUsdtStr)) {
                    oldBtcUsdt = btcUsdt;
                    redisService.set(SYMBOL_BTC_USDT, oldBtcUsdt.toString());
                } else {
                    oldBtcUsdt = new BigDecimal(oldBtcUsdtStr);
                }
                if (btcUsdt.compareTo(oldBtcUsdt.add(BigDecimal.valueOf(usdtInterval))) > 0
                        || btcUsdt
                        .compareTo(oldBtcUsdt.subtract(BigDecimal.valueOf(usdtInterval))) < 0) {
                    redisService.set(SYMBOL_BTC_USDT, btcUsdt.toString());
                    emailSend.sendEmailHtml(notifyMailUserName, "OKEX币币行情BTC/USDT:" + btcUsdt + "USDT", "当前BTC:" + btcUsdt.multiply(new BigDecimal(rate)).toString() + " CNY");
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
    @Override
    public void grab() {
        Date now = new Date();
        List<IndexInfo> indexInfoList = indexInfoMapper.listAvaidlIndexInfo();
        for (IndexInfo indexInfo : indexInfoList) {
            String paramSymbol = indexInfo.getIndexSymbol().toLowerCase().replace("-", "_");
            try {
                String index = futureClientFacade.future_index(paramSymbol);
                insertContractPirceIndexOkex(indexInfo.getIndexSymbol(),new BigDecimal(index),now);
            } catch (HttpException e) {
                LOG.error("okex["+indexInfo.getIndexSymbol()+"]指数获取失败", e);
            } catch (IOException e) {
                LOG.error("okex["+indexInfo.getIndexSymbol()+"]指数获取失败", e);
            }
        }
    }

    /**
     * 插入okex价格表
     * @param indexSymbol
     * @param price
     * @param now
     */
    private void insertContractPirceIndexOkex(String indexSymbol,BigDecimal price,Date now){
        contractPriceIndexOkexMapper.insertSelective(new ContractPriceIndexOkex(indexSymbol,price,now,null));
    }
}
