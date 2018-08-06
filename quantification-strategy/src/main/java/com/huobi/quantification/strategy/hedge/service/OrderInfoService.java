package com.huobi.quantification.strategy.hedge.service;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.util.BigDecimalUtils;
import com.huobi.quantification.dto.SpotCancleAllOrderReqDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.strategy.hedge.entity.StartHedgingParam;

@Component
public class OrderInfoService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotOrderService spotOrderService;

	@Autowired
	MarketInfoService marketInfoService;
	@Autowired
	CommonService commonService;

	/**
	 * 下单
	 *
	 * @param priceMap          下单价格
	 * @param comparePosition   -1 卖 1买
	 * @param startHedgingParam
	 * @param position          寸头
	 * @return
	 */
	public Boolean placeHuobiSpotOrder(StartHedgingParam startHedgingParam, BigDecimal position) {

		// 3. 获取买一卖一价格
		logger.info("3. 获取交易对  {} 买一卖一价格", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin());
		SpotDepthReqDto spotDepthReqDto = new SpotDepthReqDto();
		spotDepthReqDto.setBaseCoin(startHedgingParam.getBaseCoin());
		spotDepthReqDto.setQuoteCoin(startHedgingParam.getQuoteCoin());
		Map<String, BigDecimal> priceMap = marketInfoService.getHuoBiSpotBuyOneSellOnePrice(spotDepthReqDto);
		logger.info("3. 交易对  {} 买一卖一价格为： {} ", startHedgingParam.getBaseCoin() + startHedgingParam.getQuoteCoin(),
				JSON.toJSONString(priceMap));

		// 封装下单参数
		SpotPlaceOrderReqDto spotPlaceOrderReqDto = new SpotPlaceOrderReqDto();
		BigDecimal quantity = new BigDecimal(0.00);// 数量
		BigDecimal price = new BigDecimal(0.00); // 下单价格
		String side = "";// 买卖方向
		String orderType = "";// 买卖方式
		if (BigDecimalUtils.moreThan(position, BigDecimal.ZERO)) {// USDT足够,需要下买单，买入对应的币种
			side = "buy";
			orderType = "limit";
			price = priceMap.get("buyPrice").multiply((new BigDecimal(1).subtract(startHedgingParam.getSlippage())));
			quantity = position.divide(price, 18, BigDecimal.ROUND_DOWN)
					.divide((new BigDecimal(1).subtract(startHedgingParam.getFeeRate())), 18, BigDecimal.ROUND_DOWN)
					.abs();
		} else if (BigDecimalUtils.lessThan(position, BigDecimal.ZERO)) {// USDT不够,需要下卖单，卖出对应的币种
			side = "sell";
			orderType = "limit";
			price = priceMap.get("sellPrice").multiply((new BigDecimal(1).add(startHedgingParam.getSlippage())));
			quantity = position.divide(price, 18, BigDecimal.ROUND_DOWN).abs();
		} else {
			return false;
		}

		spotPlaceOrderReqDto.setAccountId(startHedgingParam.getSpotAccountID());
		spotPlaceOrderReqDto.setExchangeId(startHedgingParam.getSpotExchangeId());
		spotPlaceOrderReqDto.setBaseCoin(startHedgingParam.getBaseCoin());
		spotPlaceOrderReqDto.setQuoteCoin(startHedgingParam.getQuoteCoin());
		spotPlaceOrderReqDto.setPrice(price.divide(new BigDecimal(1), 2, BigDecimal.ROUND_DOWN));
		spotPlaceOrderReqDto.setQuantity(quantity);
		spotPlaceOrderReqDto.setSide(side);
		spotPlaceOrderReqDto.setOrderType(orderType);

		Boolean checkResult = commonService.checkPlaceOrderInfo(spotPlaceOrderReqDto);
		if (!checkResult) {
			return false;
		}
		logger.info("对冲下单请求参数为：  {} ", JSON.toJSONString(spotPlaceOrderReqDto));
		spotOrderService.placeOrder(spotPlaceOrderReqDto);
		return true;

	}

	public Boolean cancelOrder(Long accountId, String symbol, String side, Integer size) {
		SpotCancleAllOrderReqDto req = new SpotCancleAllOrderReqDto();
		req.setAccountId(accountId);
		req.setSide(side);
		req.setSize(size);
		req.setSymbol(symbol);
		spotOrderService.cancelAllOrder(req);
		return true;
	}

}
