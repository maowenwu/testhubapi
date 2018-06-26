package com.huobi.contract.index.api;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.CurrencyEnum;
import com.huobi.contract.index.contract.index.service.ExchangeRateService;
import com.huobi.contract.index.dao.ExchangeRateHisMapper;
import com.huobi.contract.index.dao.ExchangeRateMapper;
import com.huobi.contract.index.entity.ExchangeRate;
import com.huobi.contract.index.entity.ExchangeRateHis;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("exchangeRateCalcService")
public class ExchangeRateCalcServiceImpl implements ExchangeRateCalcService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateCalcServiceImpl.class);
    /**
     * 配置获取前14天的历史数据
     */
    @Value("${calc_rate_before_day}")
    private Integer BEFORE_DAY;
    /**
     * 与最新汇率的偏差阈值0.2%
     */
    @Value("${is_latest_rate_deviation}")
    private BigDecimal RATE_OFFSET;

    @Autowired
    private ExchangeRateHisMapper exchangeRateHisMapper;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    @Qualifier("exchangeRateService")
    private ExchangeRateService exchangeRateService;
    @Autowired
    private RedisService redisService;

    @Override
    public void calc() {
        List<String> currencyNames = CurrencyEnum.listShortNames();
        StringBuffer sb1, sb2 = null;
        //拼接两种币对（USD-CNY,CNY-USD)
        for (String currency : currencyNames) {
            sb1 = new StringBuffer(currency);
            sb1.append("-");
            sb1.append(Constant.USD_SHORTNAME);
            sb2 = new StringBuffer(Constant.USD_SHORTNAME);
            sb2.append("-");
            sb2.append(currency);
            handleExchangeRateHis(sb1.toString());
            handleExchangeRateHis(sb2.toString());
        }
    }

    /**
     * 处理指定币对的汇率流程
     * 1.获取前14天的汇率历史价格记录
     * 2.计算汇率均值
     * 3.计算汇率均值与当前使用的汇率（最新汇率）的偏差
     * 4.偏差<0.2% --> 本次计算汇率 延用当前使用的汇率
     * 5.偏差>0.2% --> 使用本次计算汇率
     *
     * @param currencySymbol
     */
    private void handleExchangeRateHis(String currencySymbol) {
        String beginTime = DateUtil.parseDateToStr(DateUtil.getSpecifiedDayBefore(BEFORE_DAY), DateUtil.DATE_FORMAT_YYYY_MM_DD);
        String endTime = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        List<ExchangeRateHis> exchangeRateHisList = exchangeRateHisMapper.listBeforeRecordBySymbol(currencySymbol, beginTime, endTime);
        if (CollectionUtils.isEmpty(exchangeRateHisList)) {
            return;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (ExchangeRateHis his : exchangeRateHisList) {
            sum = sum.add(his.getExchangeRate());
        }
        BigDecimal avgRate = sum.divide(new BigDecimal(exchangeRateHisList.size()), Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
        BigDecimal lastRate = exchangeRateService.getLastExchangeRatePrice(currencySymbol);
        if (lastRate == null) {
            saveExchangeRate(currencySymbol, avgRate);
            return;
        }
        //比较汇率与最新的汇率偏差
        BigDecimal offset = (avgRate.subtract(lastRate).abs()).divide(lastRate, Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
        if (BigDecimalUtil.lessThanOrEquals(offset, RATE_OFFSET)) {
            avgRate = lastRate;
        }
        saveExchangeRate(currencySymbol, avgRate);
    }

    private void saveExchangeRate(String symbol, BigDecimal rate) {
        //存入汇率表
        exchangeRateMapper.insertSelective(new ExchangeRate(symbol, rate, new Date(), null));
        //存入redis
        redisService.updateIndexRate(symbol, rate);
    }

}
