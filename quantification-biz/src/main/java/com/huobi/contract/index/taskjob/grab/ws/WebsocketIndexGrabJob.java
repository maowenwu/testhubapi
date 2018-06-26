package com.huobi.contract.index.taskjob.grab.ws;


import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import com.huobi.contract.index.contract.index.common.Origin;
import com.huobi.contract.index.dao.ContractPriceIndexHisMapper;
import com.huobi.contract.index.dao.ExchangeInfoMapper;
import com.huobi.contract.index.dto.ExchangeIndex;
import com.huobi.contract.index.entity.ContractPriceIndexHis;
import com.huobi.contract.index.entity.ValidEnum;
import com.huobi.contract.index.taskjob.grab.AbstractGrabJob;
import com.huobi.contract.index.ws.ConnectionStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class WebsocketIndexGrabJob extends AbstractGrabJob {

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @Autowired
    private ContractPriceIndexHisMapper contractPriceIndexHisMapper;

    @Override
    public void process(JobExecutionMultipleShardingContext context) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        genWsIndexPriceHis(ExchangeEnum.BITFINEX);
        genWsIndexPriceHis(ExchangeEnum.BITSTAMP);
        genWsIndexPriceHis(ExchangeEnum.GDAX);
        genWsIndexPriceHis(ExchangeEnum.GEMINI);
        genWsIndexPriceHis(ExchangeEnum.HUOBI);
        genWsIndexPriceHis(ExchangeEnum.POLONIEX);
        stopWatch.stop();
        logger.info("WebsocketIndexGrabJob 执行完成耗时：" + stopWatch);
    }

    private void genWsIndexPriceHis(ExchangeEnum exchangeEnum) {
        Map<String, BigDecimal> symbolPrice = redisService.getIndexRealtimePriceByExchange(exchangeEnum.getExchangeName());

        List<ExchangeIndex> allExchangeIndex = exchangeInfoMapper.getAllExchangeIndex(exchangeEnum.getExchangeName());
        //List<ExchangeIndex> allExchangeIndex = exchangeInfoMapper.getValidExchangeIndexBySymbol(exchangeEnum.getExchangeName());
        for (ExchangeIndex index : allExchangeIndex) {
            BigDecimal price = symbolPrice.get(index.getIndexSymbol());

            boolean b = ConnectionStatusManager.wsReconnecting(exchangeEnum, index.getIndexSymbol());
            logger.info("查看交易所状态：" + exchangeEnum + " :" + index.getIndexSymbol() + " " + b);
            BigDecimal rate = getExchangeRate(index);
            ContractPriceIndexHis contractPriceIndexHis = createIndexHis(price, b, index, rate);
            logger.info("WebSocket插入指数历史:exchangeName={},symbol={},contractPriceIndexHis={}", exchangeEnum.getExchangeName(), index.getIndexSymbol(), contractPriceIndexHis);
            redisService.updateWsHisIndexPrice(exchangeEnum.getExchangeName(), index.getIndexSymbol(), contractPriceIndexHis);
            contractPriceIndexHisMapper.insertSelective(contractPriceIndexHis);
        }

    }

    private ContractPriceIndexHis createIndexHis(BigDecimal price, boolean b, ExchangeIndex ei, BigDecimal rate) {
        ContractPriceIndexHis contractPriceIndexHis = createBaseIndexObj(ei);

        if (!b && rate != null && price != null) {
            contractPriceIndexHis.setStatus(ValidEnum.SUCC.getStatus());
        } else {
            contractPriceIndexHis.setStatus(ValidEnum.FAIL.getStatus());
        }

        if (rate != null && price != null) {
            contractPriceIndexHis.setRate(rate);
            contractPriceIndexHis.setTargetPrice(price.multiply(rate));
            contractPriceIndexHis.setSourcePrice(price);
        } else {
            contractPriceIndexHis.setRate(BigDecimal.ZERO);
            contractPriceIndexHis.setTargetPrice(BigDecimal.ZERO);
            contractPriceIndexHis.setSourcePrice(BigDecimal.ZERO);
        }
        return contractPriceIndexHis;
    }

    private ContractPriceIndexHis createBaseIndexObj(ExchangeIndex ei) {
        ContractPriceIndexHis contractPriceIndexHis = new ContractPriceIndexHis();
        contractPriceIndexHis.setExchangeId(ei.getExchangeId());
        contractPriceIndexHis.setSourceSymbol(ei.getSourceSymbol());
        contractPriceIndexHis.setTargetSymbol(ei.getIndexSymbol());
        contractPriceIndexHis.setTradeTime(new Date());
        contractPriceIndexHis.setInputTime(new Date());
        contractPriceIndexHis.setOrigin(Origin.WEBSOCKET.value());
        return contractPriceIndexHis;
    }


}
