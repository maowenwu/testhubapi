package com.huobi.contract.index.api;

import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.common.util.DateUtil;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeIndexWeightConfMapper;
import com.huobi.contract.index.dto.ContractPriceIndexHisCount;
import com.huobi.contract.index.entity.ExchangeIndexWeightConf;
import com.huobi.contract.index.entity.ValidEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("exchangeIsGrapCalcService")
public class ExchangeIsQualifiedCalcServiceImpl implements ExchangeIsQualifiedGrabCalcService {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeIsQualifiedCalcServiceImpl.class);

    @Value("${set_effective_ratio}")
    private BigDecimal  EFFECTIVE_OFFSET;
    @Value("${set_not_effective_ratio}")
    private BigDecimal  NOT_EFFECTIVE_OFFSET;
    /**
     * 配置获取前10分钟
     */
    @Value("${effective_before_minute}")
    private Integer BEFORE_MINUTE;

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;
    @Autowired
    private ExchangeIndexWeightConfMapper exchangeIndexWeightConfMapper;

    @Override
    public void httpQualifiedCalc() {
        //获取前10分钟的时刻
        Date minuteBefore = DateUtil.getMinuteBefore(BEFORE_MINUTE);
        //计算起始时间
        String startTime = DateUtil.parseDateToStr(minuteBefore, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        //获取时间间隔内交易所-币对总的记录数量
        List<ContractPriceIndexHisCount> contractPriceIndexHisCountList = contractPriceIndexHisMapper.listContractIndexHisCount(startTime, Origin.HTTP.value());
        //获取时间间隔内交易所-币对有点的数据记录数量
        List<ContractPriceIndexHisCount> effectiveContractIndexHisCountList = contractPriceIndexHisMapper.listEffectiveContractIndexHisCount(startTime,Origin.HTTP.value());
        for(ContractPriceIndexHisCount his:contractPriceIndexHisCountList){
            effectiveContractIndexHisCountList.forEach(effective ->{
                if(his.getExchangeId().equals(effective.getExchangeId()) && his.getIndexSymbol().equals(effective.getIndexSymbol())){
                    Long count = his.getCount();
                    Long effectCount = effective.getCount();
                    if(count.equals(0L)){
                        setExchangeIndexSymbolHttpQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.FAIL.getStatus());
                        return;
                    }
                    //有效点数占比//TODO
                    BigDecimal offset = BigDecimal.valueOf(effectCount).divide(BigDecimal.valueOf(count), Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
                    //占比小于等于10%设为无效
                    if(BigDecimalUtil.lessThanOrEquals(offset, NOT_EFFECTIVE_OFFSET)){
                        setExchangeIndexSymbolHttpQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.FAIL.getStatus());
                    }
                    //占比大于等于90%设为有效
                    if(BigDecimalUtil.moreThanOrEquals(offset, EFFECTIVE_OFFSET)){
                        setExchangeIndexSymbolHttpQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.SUCC.getStatus());
                    }
                }
            });
        }
    }

    @Override
    public void wsQualifiedCalc() {
        //获取前10分钟的时刻
        Date minuteBefore = DateUtil.getMinuteBefore(BEFORE_MINUTE);
        //计算起始时间
        String startTime = DateUtil.parseDateToStr(minuteBefore, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        //获取时间间隔内交易所-币对总的记录数量
        List<ContractPriceIndexHisCount> contractPriceIndexHisCountList = contractPriceIndexHisMapper.listContractIndexHisCount(startTime, Origin.WEBSOCKET.value());
        //获取时间间隔内交易所-币对有点的数据记录数量
        List<ContractPriceIndexHisCount> effectiveContractIndexHisCountList = contractPriceIndexHisMapper.listEffectiveContractIndexHisCount(startTime,Origin.WEBSOCKET.value());
        for(ContractPriceIndexHisCount his:contractPriceIndexHisCountList){
            effectiveContractIndexHisCountList.forEach(effective ->{
                if(his.getExchangeId().equals(effective.getExchangeId()) && his.getIndexSymbol().equals(effective.getIndexSymbol())){
                    Long count = his.getCount();
                    Long effectCount = effective.getCount();
                    if(count.equals(0L)){
                        setExchangeIndexSymbolwsQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.FAIL.getStatus());
                        return;
                    }
                    //有效点数占比//TODO
                    BigDecimal offset = BigDecimal.valueOf(effectCount).divide(BigDecimal.valueOf(count), Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
                    //占比小于等于10%设为无效
                    if(BigDecimalUtil.lessThanOrEquals(offset, NOT_EFFECTIVE_OFFSET)){
                        setExchangeIndexSymbolwsQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.FAIL.getStatus());
                    }
                    //占比大于等于90%设为有效
                    if(BigDecimalUtil.moreThanOrEquals(offset, EFFECTIVE_OFFSET)){
                        setExchangeIndexSymbolwsQualified(his.getExchangeId(),his.getIndexSymbol(),ValidEnum.SUCC.getStatus());
                    }
                }
            });
        }
    }

    /**
     * 设置交易所-币对的HTTP是否参与计算状态
     */
    private  void setExchangeIndexSymbolHttpQualified(Long exchangeId,String indexSymbol,Integer grapStatus){
        ExchangeIndexWeightConf conf  = new ExchangeIndexWeightConf();
        conf.setExchangeId(exchangeId);
        conf.setIndexSymbol(indexSymbol);
        conf.setUpdateTime(new Date());
        conf.setHttpQualified(grapStatus);
        exchangeIndexWeightConfMapper.updateIsQualifiedByExchangeIDAndIndexSymbol(conf);
    }
    /**
     * 设置交易所-币对的WS是否参与计算状态
     */
    private  void setExchangeIndexSymbolwsQualified(Long exchangeId,String indexSymbol,Integer grapStatus){
        ExchangeIndexWeightConf conf  = new ExchangeIndexWeightConf();
        conf.setExchangeId(exchangeId);
        conf.setIndexSymbol(indexSymbol);
        conf.setUpdateTime(new Date());
        conf.setWsQualified(grapStatus);
        System.out.println(conf);
        exchangeIndexWeightConfMapper.updateIsQualifiedByExchangeIDAndIndexSymbol(conf);
    }
}
