package com.huobi.quantification.strategy;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.StrategyApplication;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.api.future.FutureContractService;
import com.huobi.quantification.api.spot.SpotAccountService;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanAccountHistoryMapper;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;
import com.huobi.quantification.dto.SpotBalanceReqDto;
import com.huobi.quantification.dto.SpotBalanceRespDto;
import com.huobi.quantification.dto.SpotBalanceRespDto.DataBean;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.strategy.hedging.AccountUtil;
import com.huobi.quantification.strategy.hedging.MarketUtil;
import com.huobi.quantification.strategy.hedging.StartHedging;
import com.huobi.quantification.strategy.hedging.StartHedgingParam;
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
	OrderContext orderContext;

	@Autowired
	FutureContractService futureContractService;

	@Autowired
	AccountUtil accountUtil;

	@Autowired
	MarketUtil marketUtil;

	@Autowired
	StartHedging startHedging;
	@Autowired
	FutureAccountService  futureAccountService;
	
	@Autowired
	QuanAccountHistoryMapper quanAccountHistoryMapper;

	// 1.撤掉币币账户所有未成交订单 根据交易对撤销
	@Test
	public void test1() {
		ServiceResult<Object> result = spotOrderService.cancelOrder(4295363L, "btcusdt", null, null);
		logger.info("取消订单返回的结果为：{}", JSON.toJSONString(result));
	}

	// 2.计算当前的两个账户总的净头寸USDT
	@Test
	public void test2() {
		String coinType = "usdt";
		SpotBalanceReqDto spotBalanceReqDto = new SpotBalanceReqDto();
		spotBalanceReqDto.setAccountId(4295363L);
		spotBalanceReqDto.setExchangeId(ExchangeEnum.HUOBI.getExId());
		spotBalanceReqDto.setMaxDelay(1000 * 60 * 60);
		spotBalanceReqDto.setTimeout(1000 * 10);
		BigDecimal spotCurrentUSDTBalance = accountUtil.getHuobiSpotCurrentBalance(spotBalanceReqDto, coinType)
				.getAvailable();
		logger.info("1.获取火币账户余额=================================" + spotCurrentUSDTBalance);

		ServiceResult<BigDecimal> result = futureContractService.getExchangeRateOfUSDT2USD();
		BigDecimal rateOfUSDT2USD = result.getData();
		logger.info("2.USDT/USD的利率为：{}", result.getData());
		if (null == rateOfUSDT2USD) {// 拿到的利率为空，不能进行下面操作
			// return;
		}

		FutureBalanceReqDto futureBalanceReqDto = new FutureBalanceReqDto();
		futureBalanceReqDto.setAccountId(11111111111111L);
		futureBalanceReqDto.setCoinType("usd");
		futureBalanceReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
		futureBalanceReqDto.setMaxDelay(100000l);
		futureBalanceReqDto.setTimeout(100000l);
		accountUtil.getOkFutureBalance(futureBalanceReqDto);
		FutureBalance futureBalance = accountUtil.getOkFutureBalance(futureBalanceReqDto);
		BigDecimal okFutureBalance = futureBalance.getMarginBalance();
		logger.info("ok期货账户期末(即当前)余额USDT为：{}", okFutureBalance);

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

	// 计算下单价格
	private void getPrice() {

	}

	// 计算下单数量
	private void getAccount() {

	}

	// 获取买一卖一价格
	@Test
	public void getHuoBiSpotBuyOneSellOnePrice() {
		SpotDepthReqDto spotDepthReqDto = new SpotDepthReqDto();
		spotDepthReqDto.setBaseCoin("btc");
		spotDepthReqDto.setExchangeId(1);
		spotDepthReqDto.setMaxDelay(10000L);
		spotDepthReqDto.setQuoteCoin("usdt");
		spotDepthReqDto.setTimeout(10000L);
		marketUtil.getHuoBiSpotBuyOneSellOnePrice(spotDepthReqDto);
	}

	// 获取滑头

	// 测试ok合约账户余额
	@Test
	public void getOkFutureBalance() {
		FutureBalanceReqDto futureBalanceReqDto = new FutureBalanceReqDto();
		futureBalanceReqDto.setAccountId(2);
		futureBalanceReqDto.setCoinType("btc");
		futureBalanceReqDto.setExchangeId(ExchangeEnum.OKEX.getExId());
		futureBalanceReqDto.setMaxDelay(100000l);
		futureBalanceReqDto.setTimeout(100000l);
		FutureBalance futureBalance = accountUtil.getOkFutureBalance(futureBalanceReqDto);
		BigDecimal okFutureBalance = futureBalance.getMarginBalance();
		logger.info("ok期货账户期末(即当前)余额USDT为：{}", okFutureBalance);
	}

	@Test
	public void start() {
		StartHedgingParam startHedgingParam = new StartHedgingParam();
		startHedgingParam.setBaseCoin("btc");
		startHedgingParam.setFeeRate(new BigDecimal(0));
		startHedgingParam.setQuoteCoin("usdt");
		startHedgingParam.setSlippage(new BigDecimal(0));
		startHedgingParam.setSpotAccountID(4295363L);
		startHedgingParam.setSpotExchangeId(1);
		startHedging.startNormal(startHedgingParam);
	}
	
	@Test
	public void getFutureBalance() {
		FuturePositionReqDto balanceReqDto=new FuturePositionReqDto();
		balanceReqDto.setExchangeId(2);
		balanceReqDto.setAccountId(2);
		//balanceReqDto.setCoinType(coinType);
		balanceReqDto.setMaxDelay(100000l);
		balanceReqDto.setTimeout(100000l);
		ServiceResult<FuturePositionRespDto> result=futureAccountService.getPosition(balanceReqDto);
		logger.info("获取账户余额(权益)"+JSON.toJSONString(result));
	}
	
	
	
	

}
