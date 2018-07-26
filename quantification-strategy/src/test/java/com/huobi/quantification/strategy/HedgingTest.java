package com.huobi.quantification.strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto.DataBean;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.hedging.AccountUtil;
import com.huobi.quantification.strategy.hedging.MarketUtil;
import com.huobi.quantification.strategy.order.OrderContext;
import com.huobi.quantification.strategy.order.entity.FutureBalance;

@SpringBootTest(classes = StrategyApplication.class)
@RunWith(SpringRunner.class)
public class HedgingTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpotOrderService spotOrderService;
	
	@Autowired
	SpotAccountService spotAccountService;
	
	@Autowired
	OrderContext  orderContext ;
	
	@Autowired
	FutureContractService futureContractService;
	
	@Autowired
	AccountUtil accountUtil;
	
	@Autowired 
	MarketUtil marketUtil;
	
	


	//1.撤掉币币账户所有未成交订单  根据交易对撤销
	@Test
	public void test1() {
		ServiceResult<Object>  result=spotOrderService.cancelOrder(4295363L, "btcusdt", null, null);
		logger.info("取消订单返回的结果为：{}",JSON.toJSONString(result));
	}
	
	//2.计算当前的两个账户总的净头寸USDT
	@Test
	public void test2() {
		String coinType="usdt";
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(4295363L);
		spotBalanceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotBalanceReqDto.setMaxDelay(1000 * 60 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		BigDecimal spotCurrentUSDTBalance=accountUtil.getHuobiSpotCurrentUSDTBalance(spotBalanceReqDto, coinType).getAvailable();
		logger.info("1.获取火币账户余额================================="+spotCurrentUSDTBalance);
		
		ServiceResult<BigDecimal> result=futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD =result.getData();
		logger.info("2.USDT/USD的利率为：{}",result.getData());
		if(null ==rateOfUSDT2USD) {//拿到的利率为空，不能进行下面操作
			//return;
		}
		
		
		FutureBalanceReqDto futureBalanceReqDto=new FutureBalanceReqDto();
		futureBalanceReqDto.setAccountId(11111111111111L);
		futureBalanceReqDto.setCoinType("usd");
		futureBalanceReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
		futureBalanceReqDto.setMaxDelay(100000l);
		futureBalanceReqDto.setTimeout(100000l);
		accountUtil.getOkFutureBalance(futureBalanceReqDto) ;
		FutureBalance futureBalance=orderContext.getFutureBalance("usd");
		BigDecimal okFutureBalance=futureBalance.getMarginBalance();
		logger.info("ok期货账户期末(即当前)余额USDT为：{}",okFutureBalance);
		
		
		BigDecimal temp=new BigDecimal(0.001);
		BigDecimal position=accountUtil.calPosition(temp, temp, temp, temp, temp);
		int compareResutl=position.compareTo(new BigDecimal(0));
		//-1, 0, or 1
		if(-1==compareResutl) {
			
		}else {
			
		}
		
		
		
	}	
	
	
	//在币币账户，对净头寸进行对冲   --下单
	@Test
	public void test3() {
	}
	
	
	
	
	
	// 计算火币现货币币账户期末(即当前)余额USDT
	private DataBean getHuobiSpotCurrentUSDTBalance(int exchangeId, long accountId, String coin) {
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(accountId);
		spotBalanceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotBalanceReqDto.setMaxDelay(1000 * 60 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		ServiceResult<SpotBalanceRespDto> result = spotAccountService.getBalance(spotBalanceReqDto);
		logger.info("计算币币账户期末余额USDT为{}", JSON.toJSONString(result));
		DataBean dataBean = result.getData().getData().get(coin);
		return dataBean;
	}
	
	
	
	//计算下单价格
	private void getPrice() {

	}
	
	
	//计算下单数量
	private void getAccount() {

	}
	
	
	@Test
	public void getHuoBiSpotBuyOneSellOnePrice() {
		SpotDepthReqDto spotDepthReqDto=new SpotDepthReqDto();
		spotDepthReqDto.setBaseCoin("btc");
		spotDepthReqDto.setExchangeId(1);
		spotDepthReqDto.setMaxDelay(10000L);
		spotDepthReqDto.setQuoteCoin("usdt");
		spotDepthReqDto.setTimeout(10000L);
		marketUtil.getHuoBiSpotBuyOneSellOnePrice(spotDepthReqDto);
	}
	
	
	
	
}
