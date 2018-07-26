package com.huobi.quantification.strategy.order;

import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.StrategyOrderConfigMapper;
import com.huobi.quantification.dto.*;
import com.huobi.quantification.entity.StrategyOrderConfig;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.order.entity.DepthBook;
import com.huobi.quantification.strategy.order.entity.FutureBalance;
import com.huobi.quantification.strategy.order.entity.FuturePosition;
import com.huobi.quantification.strategy.order.entity.SpotBalance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class OrderContext {

    @Autowired
    private SpotMarketService spotMarketService;

    @Autowired
    private FutureContractService futureContractService;

    @Autowired
    private SpotAccountService spotAccountService;

    @Autowired
    private FutureAccountService futureAccountService;

    @Autowired
    private StrategyOrderConfigMapper strategyOrderConfigMapper;

    public DepthBook getDepth(String symbol) {
        String[] split = symbol.split("_");
        SpotDepthReqDto reqDto = new SpotDepthReqDto();
        reqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
        reqDto.setBaseCoin(split[0]);
        reqDto.setQuoteCoin(split[1]);
        reqDto.setTimeout(100);
        reqDto.setMaxDelay(3000);
        ServiceResult<SpotDepthRespDto> spotDepth = spotMarketService.getSpotDepth(reqDto);
        SpotDepthRespDto.DataBean data = spotDepth.getData().getData();
        DepthBook depthBook = new DepthBook();
        data.getAsks().forEach(e -> {
            depthBook.getAsks().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
        });
        data.getBids().forEach(e -> {
            depthBook.getBids().add(new DepthBook.Depth(e.getPrice(), e.getAmount()));
        });
        return depthBook;
    }

    public BigDecimal getExchangeRateOfUSDT2USD() {
        ServiceResult<BigDecimal> exchangeRateOfUSDT2USD = futureContractService.getExchangeRateOfUSDT2USD();
        return exchangeRateOfUSDT2USD.getData();
    }


    public SpotBalance getSpotBalance(String coinType) {
        ServiceResult<SpotBalanceRespDto> balance = spotAccountService.getBalance(null);
        Map<String, SpotBalanceRespDto.DataBean> data = balance.getData().getData();
        SpotBalanceRespDto.DataBean dataBean = data.get(coinType);
        if (dataBean != null) {
            SpotBalance spotBalance = new SpotBalance();
            BeanUtils.copyProperties(dataBean, spotBalance);
            return spotBalance;
        } else {
            return null;
        }
    }


    public FutureBalance getFutureBalance(String coinType) {
        ServiceResult<FutureBalanceRespDto> balance = futureAccountService.getBalance(null);
        Map<String, FutureBalanceRespDto.DataBean> data = balance.getData().getData();
        FutureBalanceRespDto.DataBean dataBean = data.get(coinType);
        if (dataBean != null) {
            FutureBalance futureBalance = new FutureBalance();
            BeanUtils.copyProperties(dataBean, futureBalance);
            return futureBalance;
        } else {
            return null;
        }
    }

    public FuturePosition getFuturePosition(String coinType) {
        ServiceResult<FuturePositionRespDto> position = futureAccountService.getPosition(null);
        Map<String, List<FuturePositionRespDto.DataBean>> data = position.getData().getData();
        List<FuturePositionRespDto.DataBean> beanList = data.get(coinType);
        FuturePosition futurePosition = new FuturePosition();
        beanList.forEach(e -> {
            if (e.getLongAmount() != null) {
                FuturePosition.LongPosi longPosi = new FuturePosition.LongPosi();
                BeanUtils.copyProperties(e, longPosi);
                futurePosition.setLongPosi(longPosi);
            }
            if (e.getShortAmount() != null) {
                FuturePosition.ShortPosi shortPosi = new FuturePosition.ShortPosi();
                BeanUtils.copyProperties(e, shortPosi);
                futurePosition.setShortPosi(shortPosi);
            }
        });
        return futurePosition;
    }

    public void init() {

    }

    public StrategyOrderConfig getStrategyOrderConfig() {
        StrategyOrderConfig orderConfig = strategyOrderConfigMapper.selectByPrimaryKey(1);
        return orderConfig;
    }
}
