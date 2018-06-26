package com.huobi.contract.index.monitor.impl;

import com.google.common.collect.Maps;
import com.huobi.contract.index.common.util.BigDecimalUtil;
import com.huobi.contract.index.common.util.Constant;
import com.huobi.contract.index.dao.ContractPriceIndexOkexMapper;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.entity.ContractPriceIndex;
import com.huobi.contract.index.entity.ContractPriceIndexOkex;
import com.huobi.contract.index.monitor.common.MonitorConfig;
import com.huobi.contract.index.monitor.service.IndexMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *   指数实时价格，与同时间Okex指数价格偏差过大
 */
@Service("indexPriceWithOkexPriceMoitor")
public class IndexPriceWithOkexPriceMoitor extends BaseIndexMonitorService implements IndexMonitorService {

    @Autowired
    private ContractPriceIndexOkexMapper contractPriceIndexOkexMapper;
    @Value("${index.price.okex.price.deviation}")
    private BigDecimal indexPriceOkexPriceDeviation;
    @Value("${index.price.okex.price.deviation.level}")
    private String indexPriceOkexPriceLevel;

    private final static String VARIETIES = "varieties";
    private final static String PERCENTAGE = "percentage";

    @Override
    public void monitor() {
        //最新指数指数价格信息
        List<ContractPriceIndex> priceIndexList = listLastIndexPrice();
        //最新okex指数价格信息
        List<ContractPriceIndexOkex> lastOkexPriceList = contractPriceIndexOkexMapper.listLastOkexPrice();
        handleAlarm(priceIndexList,lastOkexPriceList);
    }
    /**
     * 报警
     */
    protected void handleAlarm(List<ContractPriceIndex> priceIndexList, List<ContractPriceIndexOkex> lastOkexPriceList ){
        Map<String,Object> model = Maps.newHashMap();
        for(ContractPriceIndex indexPrice : priceIndexList){
            lastOkexPriceList.stream().filter(okex->indexPrice.getIndexSymbol().equals(okex.getIndexSymbol())).forEach(ok-> {
                BigDecimal deviation = compilePriceDeviation(indexPrice.getIndexPrice(),ok.getIndexPrice());
                if(BigDecimalUtil.moreThanOrEquals(deviation, indexPriceOkexPriceDeviation)){
                    //报警
                    model.put(VARIETIES, indexPrice.getIndexSymbol());
                    model.put(PERCENTAGE, indexPriceOkexPriceDeviation);
                    handleAlarmByLevel(model);
                }
            });
//            lastOkexPriceList.forEach(okex->{
//                if(indexPrice.getIndexSymbol().equals(okex.getIndexSymbol())){
//
//                }
//            });
        }
    }


    /**
     * 比较两个价格的偏差比例（高价-低价)/低价
     * @param price1
     * @param price2
     * @return
     */
    protected BigDecimal compilePriceDeviation(BigDecimal price1,BigDecimal price2){
       BigDecimal lowPrice =  BigDecimalUtil.moreThan(price1, price2) ? price2 : price1;
       BigDecimal deviation = price1.subtract(price2).abs().divide(lowPrice, Constant.PRICE_DECIMALS_NUM, BigDecimal.ROUND_DOWN);
       return deviation;
    }



    @Override
    protected String getSubject() {
        return MonitorConfig.indexPriceWithOkexPriceMoitorSubject;
    }

    @Override
    protected String getTemplateContent() {
        return MonitorConfig.indexPriceWithOkexPriceMoitor;
    }

    @Override
    protected List<String> getAlarmLevel() {
        return Arrays.asList(indexPriceOkexPriceLevel.split(","));
    }
}
