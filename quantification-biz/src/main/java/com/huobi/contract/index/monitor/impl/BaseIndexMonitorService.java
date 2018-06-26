package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Lists;
import com.huobi.contract.index.api.BaseContractIndexService;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.CurrencyEnum;
import com.huobi.contract.index.contract.index.common.EmailType;
import com.huobi.contract.index.contract.index.service.ExchangeRateService;
import com.huobi.contract.index.dao.EmailSendHistoryMapper;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.dao.SmsSendHistoryMapper;
import com.huobi.contract.index.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class  BaseIndexMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(BaseIndexMonitorService.class);
    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Autowired
    private BaseContractIndexService baseContractIndexService;
    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private EmailSendHistoryMapper emailSendHistoryMapper;
    @Autowired
    private SmsSendHistoryMapper smsSendHistoryMapper;
    @Value("${email.from}")
    private String emailFrom;
    @Value("${email.to}")
    private String emailTo;
    @Value("${email.cc}")
    private String emailCc;
    @Value("${sms.destination}")
    private String smsDestination;
    private final static String SMS_CLIENT_ID = "contract-index";
    private final static String EMAIL_STATUS_WAITING = "waiting";
    /**
     * 告警内容模板中的告警时间key
     */
    private final static String ALARM_TIME_TEMPLATE_KEY = "time";

    /**
     * 获取所有币对的最新指数价格
     * @return
     */
    protected List<ContractPriceIndex> listLastIndexPrice(){
        List<IndexInfo> indexInfoList = indexInfoMapper.listAvaidlIndexInfo();
        List<ContractPriceIndex> priceIndexList = Lists.newArrayList();
        for(IndexInfo index:indexInfoList){
            ContractPriceIndex priceIndex = baseContractIndexService.getLastContractIndex(index.getIndexSymbol());
            if(priceIndex==null){
                priceIndex = new ContractPriceIndex();
                priceIndex.setIndexSymbol(index.getIndexSymbol());
            }
            priceIndexList.add(priceIndex);
        }
        return priceIndexList;
    }

    /**
     * 获取最新的汇率
     * 1.汇率如果不存在，存入symbol
     * @return
     */
    protected List<ExchangeRate> listLastExchangeRate(){
        List<String> currencyNames = CurrencyEnum.listShortNames();
        List<ExchangeRate> exchangeRateList = Lists.newArrayList();
        for(String name:currencyNames){
            String rateSymbol = Constant.USD_SHORTNAME+"-"+name;
            ExchangeRate exchangeRate = exchangeRateService.getLastExchangeRate(rateSymbol);
            if(exchangeRate == null){
                exchangeRate = new ExchangeRate();
                exchangeRate.setExchangeSymbol(rateSymbol);
            }
            exchangeRateList.add(exchangeRate);
        }
        return exchangeRateList;
    }
    /**
     * 1.根据model创建邮件或短信记录对象
     * 2.插入邮件或短信记录表
     * @param model
     */
    protected void handleAlarmByLevel(Map<String,Object> model){
        List<String> alertLevelList = getAlarmLevel();
        Date alarmTime = new Date();
        String content = getContentFromTemplate(model);
        String subject = getSubject();
        if(alertLevelList.contains(Constant.INDEX_MONITOR_EMAIL)){
            //邮件报警
            EmailSendHistory emailSendHistory = createDefaultObjEmailSendHistory(model);
            emailSendHistory.setInputTime(alarmTime);
            emailSendHistory.setContent(content);
            emailSendHistory.setSubject(subject);
            insertIntoEmailSendHistory(emailSendHistory);
        }
        if(alertLevelList.contains(Constant.INDEX_MONITOR_MESSAGE)){
            //短信报警
            SmsSendHistory smsSendHistory = createDefaultObjSmsSendHistory(model);
            smsSendHistory.setInputTime(alarmTime);
            smsSendHistory.setContent(content);
            insertIntoSmsSendHistory(smsSendHistory);
        }
    }
    /**
     * 插入邮件记录表
     */
    protected void insertIntoEmailSendHistory(EmailSendHistory history){
        emailSendHistoryMapper.insertSelective(history);
    }
    protected EmailSendHistory createDefaultObjEmailSendHistory(Map<String,Object> model){


        EmailSendHistory email = new EmailSendHistory();
        email.setInputTime(new Date());
        email.setEmailFrom(emailFrom);
        email.setEmailTo(emailTo);
        email.setEmailCc(emailCc);
        email.setStatus(EMAIL_STATUS_WAITING);
        email.setEmailType(EmailType.TEXT.value());
        return email;
    }
    /**
     * 插入短信记录表
     */
    protected void insertIntoSmsSendHistory(SmsSendHistory history){
        smsSendHistoryMapper.insertSelective(history);
    }
    protected SmsSendHistory createDefaultObjSmsSendHistory(Map<String,Object> model){
        SmsSendHistory sms = new SmsSendHistory();
        sms.setInputTime(new Date());
        sms.setClientId(null);
        sms.setDestination(smsDestination);
        sms.setDealTimes(null);
        sms.setClientId(SMS_CLIENT_ID);
        sms.setStatus(EMAIL_STATUS_WAITING);
        return sms;
    }

    /**
     * 根据参数填充模板
     * @return
     */
    protected String getContentFromTemplate(Map<String,Object> model){
        String content = getTemplateContent();
        model.put(ALARM_TIME_TEMPLATE_KEY, DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        Iterator<Map.Entry<String,Object>> it =  model.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Object> entry = it.next();
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(entry.getKey());
            sb.append("}");
            content = content.replace(sb.toString(), entry.getValue().toString());
        }
        return content;
    }

    /**
     * 获取主题
     * @return
     */
    protected abstract String getSubject();

    /**
     * 获取模板
     * @return
     */
    protected abstract String getTemplateContent();

    protected abstract List<String> getAlarmLevel();
}
