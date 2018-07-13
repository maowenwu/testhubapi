package com.huobi.quantification.provider;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.dto.FutureCurrentPriceRespDto;
import com.huobi.quantification.dto.SpotCurrentPriceReqDto;
import com.huobi.quantification.dto.SpotCurrentPriceRespDto;
import com.huobi.quantification.entity.QuanTradeFuture;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.huobi.response.TradeResponse;
import com.huobi.quantification.service.redis.RedisService;


@Service
public class SpotMarketServiceImpl implements SpotMarketService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RedisService redisService;

	@Override
	public ServiceResult<SpotCurrentPriceRespDto> getCurrentPrice(SpotCurrentPriceReqDto currentPriceReqDto) {
		ServiceResult<SpotCurrentPriceRespDto> serviceResult = new ServiceResult<>();
		try {
			SpotCurrentPriceRespDto currentPriceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新价格
                	TradeResponse tradeSpot = redisService.getHuobiCurrentPrice(currentPriceReqDto.getExchangeId(), getSymbol(currentPriceReqDto.getExchangeId(), 
                			currentPriceReqDto.getBaseCoin(), currentPriceReqDto.getQuoteCoin()));
                    if (tradeSpot == null) {
                        continue;
                    }
                    Date ts = tradeSpot.getTick().getTs();
                    System.out.println("currentPriceRespDto时间：" + DateUtils.format(ts, "yyyy-MM-dd HH:mm:ss"));
                    System.out.println("当前时间：" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    if (DateUtils.withinMaxDelay(ts, currentPriceReqDto.getMaxDelay())) {
                    	SpotCurrentPriceRespDto respDto = new SpotCurrentPriceRespDto();
                        respDto.setTs(ts);
                        respDto.setCurrentPrice(tradeSpot.getTick().getData().getPrice());
                        return respDto;
                    } else {
                        continue;
                    }
                }
                return null;
            }, currentPriceReqDto.getTimeout());
            serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
            serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
            serviceResult.setData(currentPriceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult.setCode(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
            serviceResult.setMessage(ServiceErrorEnum.TIMEOUT_ERROR.getMessage());
        }
        return serviceResult;
	}
	
	 private String getSymbol(int exchangeId, String baseCoin, String quoteCoin) {
	        if (exchangeId == ExchangeEnum.HUOBI.getExId()) {
	            return baseCoin.toLowerCase() + quoteCoin.toLowerCase();
	        } else {
	            throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
	        }
	    }

}
