package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.FeeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.dto.DepthBookInfo;
import cn.huobi.framework.dto.StrategyInstanceDetailInfo;
import cn.huobi.framework.dto.StrategyInstanceInfo;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;
import cn.huobi.framework.service.FutureOrderService;
import cn.huobi.framework.service.sysUser.CommonService;
import cn.huobi.framework.util.CollectorsUtil;
import com.huobi.quantification.api.spot.SpotMarketService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.*;
import com.huobi.quantification.dto.SpotDepthReqDto;
import com.huobi.quantification.dto.SpotDepthRespDto;
import com.huobi.quantification.entity.*;
import com.huobi.quantification.enums.ExchangeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.record.crypto.Biff8Cipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    QuanAccountAssetMapper quanAccountAssetMapper;

    @Autowired
    QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Autowired
    StrategyInstanceConfigMapper strategyInstanceConfigMapper;

    @Autowired
    StrategyRiskHistoryMapper strategyRiskHistoryMapper;

    @Autowired
    FutureOrderService futureOrderService;

    @Autowired
    QuanExchangeConfigMapper quanExchangeConfigMapper;

    private static Map<String, QuanExchangeConfig> configMap = new HashMap<>();


    @Override
    public List<StrategyInstanceInfo> findStrategyInstanceInfo(String futureBaseCoin) {
        List<StrategyInstanceInfo> resultList = new ArrayList<>();
        StrategyInstanceConfig strategyInstanceConfig = new StrategyInstanceConfig();
        strategyInstanceConfig.setFutureBaseCoin(futureBaseCoin);
        //获取到一组实例
        List<StrategyInstanceConfig> list = strategyInstanceConfigMapper.selectList(strategyInstanceConfig);
        //查询每一个实例总盈亏
        for (StrategyInstanceConfig temp : list) {
            StrategyInstanceInfo strategyInstanceInfo = new StrategyInstanceInfo();
            StrategyRiskHistory entity = strategyRiskHistoryMapper.selectLatestByInstanceIdCoin(temp.getInstanceId(), temp.getFutureBaseCoin());
            //todo 运行状态要根据心跳判断
            if (null == entity) {
                continue;
            }
            strategyInstanceInfo.setId(temp.getId());//主键id
            strategyInstanceInfo.setInstanceGroup(temp.getInstanceGroup());//账户组别
            strategyInstanceInfo.setInstanceEnable(temp.getInstanceEnable());//运行状态
            strategyInstanceInfo.setTotalProfit(entity.getTotalProfit());////总盈亏
            strategyInstanceInfo.setFutureContractCode(temp.getFutureContractCode());//合约代码
            resultList.add(strategyInstanceInfo);
        }
        return resultList;
    }


    @Override
    public StrategyInstanceConfig findStrategyInstanceBaseInfo(Integer id) {
        StrategyInstanceConfig strategyInstanceConfig = strategyInstanceConfigMapper.selectByPrimaryKey(id);
        return strategyInstanceConfig;
    }

    //todo
    @Override
    public Map<String, Object> findAccountInfo(Integer id) {
        Map<String, Object> map = new HashMap<>();
        StrategyInstanceConfig strategyInstanceConfig = strategyInstanceConfigMapper.selectByPrimaryKey(id);

        QuanAccountAsset spotQuoteCoinAssert = quanAccountAssetMapper.selectLatestByAccountIdCoinType(strategyInstanceConfig.getSpotAccountId(), strategyInstanceConfig.getSpotQuotCoin());
        QuanAccountAsset spotBaseCoinAssert = quanAccountAssetMapper.selectLatestByAccountIdCoinType(strategyInstanceConfig.getSpotAccountId(), strategyInstanceConfig.getSpotBaseCoin());
        QuanAccountFutureAsset futureBaseCoinAsset = quanAccountFutureAssetMapper.selectLastByAccountFutureIdCoinType(strategyInstanceConfig.getFutureAccountId(), strategyInstanceConfig.getFutureBaseCoin());
        QuanAccountFuturePosition futureBaseCoinPosition = quanAccountFuturePositionMapper.selectLatestByAccountFutureIdBaseCoin(strategyInstanceConfig.getFutureAccountId(), strategyInstanceConfig.getFutureBaseCoin());
        if (null != spotQuoteCoinAssert) {
            map.put("spotQuoteCoinAmount", spotQuoteCoinAssert.getTotal());//持币量 （BTC）
        }
        if (null != spotBaseCoinAssert) {
            map.put("spotBaseCoinAmount", spotBaseCoinAssert.getTotal());//资金量（USDT）
        }
        if (null != futureBaseCoinPosition) {
            map.put("futurePositionAmount", futureBaseCoinPosition.getAmount());//持仓量（张）
        }
        if (null != futureBaseCoinAsset) {
            map.put("futureAssetAmount", futureBaseCoinAsset.getMarginBalance());//持币量 （BTC）
        }
        return map;
    }

    @Override
    public Map<String, BigDecimal> findOrderData(Integer id) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        //策略信息
        StrategyInstanceConfig strategyInstanceConfig = strategyInstanceConfigMapper.selectByPrimaryKey(id);

        //查询该实例下的订单信息
        List<QuanOrderFuture> list = futureOrderService.selectUnfinishedByInstanceId(strategyInstanceConfig.getInstanceId());

        BigDecimal buyOpenRemainingQty = BigDecimal.ZERO;     //买入开多张数
        BigDecimal buyOpenCoinNum = BigDecimal.ZERO;//买入开多币数
        BigDecimal buyOpenOrderTotal = BigDecimal.ZERO;//买入开多订单数
        BigDecimal buyCloseRemainingQty = BigDecimal.ZERO;     //买入平空张数
        BigDecimal buyCloseCoinNum = BigDecimal.ZERO;//买入平空币数
        BigDecimal buyCloseOrderTotal = BigDecimal.ZERO;//买入平空订单数
        BigDecimal buyRemainingQtyTotal = BigDecimal.ZERO;     //买盘总数  张数
        BigDecimal buyCoinNumTotal = BigDecimal.ZERO;     //买盘总数  币数
        BigDecimal buyOrderTotal = BigDecimal.ZERO;     //买盘总数  订单数


        BigDecimal sellOpenRemainingQty = BigDecimal.ZERO;     //卖出开多张数
        BigDecimal sellOpenCoinNum = BigDecimal.ZERO;//卖出开多币数
        BigDecimal sellOpenOrderTotal = BigDecimal.ZERO;//卖出开多订单数
        BigDecimal sellCloseRemainingQty = BigDecimal.ZERO;     //卖出平空张数
        BigDecimal sellCloseCoinNum = BigDecimal.ZERO;//卖出平空币数
        BigDecimal sellCloseOrderTotal = BigDecimal.ZERO;//卖出平空订单数
        BigDecimal sellRemainingQtyTotal = BigDecimal.ZERO;     //买盘总数  张数
        BigDecimal sellCoinNumTotal = BigDecimal.ZERO;     //卖盘总数  币数
        BigDecimal sellOrderTotal = BigDecimal.ZERO;     //卖盘总数  订单数

        //统计订单数  开始
        Map<Integer, Map<Integer, Long>> countMap = list.stream().
                collect(Collectors.groupingBy(QuanOrderFuture::getSide,
                        Collectors.groupingBy(QuanOrderFuture::getOffset,
                                Collectors.counting())));
        buyOpenOrderTotal = new BigDecimal(countMap.get(1).get(1));
        buyCloseOrderTotal = new BigDecimal(countMap.get(1).get(2));
        sellOpenOrderTotal = new BigDecimal(countMap.get(2).get(1));
        sellCloseOrderTotal = new BigDecimal(countMap.get(2).get(2));
        buyOrderTotal = buyOpenOrderTotal.add(buyCloseOrderTotal);
        sellOrderTotal = sellOpenOrderTotal.add(sellCloseOrderTotal);
        //统计订单数  结束


        //统计张数 开始
        Map<Integer, Map<Integer, BigDecimal>> qtyMap = list.stream().
                collect(Collectors.groupingBy(QuanOrderFuture::getSide,
                        Collectors.groupingBy(QuanOrderFuture::getOffset,
                                CollectorsUtil.summingBigDecimal(QuanOrderFuture::getRemainingQty))));
        buyOpenRemainingQty = qtyMap.get(1).get(1);
        buyCloseRemainingQty = qtyMap.get(1).get(2);
        sellOpenRemainingQty = qtyMap.get(2).get(1);
        sellCloseRemainingQty = qtyMap.get(2).get(2);
        buyRemainingQtyTotal = buyOpenRemainingQty.add(buyCloseRemainingQty);
        sellRemainingQtyTotal = sellOpenRemainingQty.add(sellCloseRemainingQty);
        //统计张数 结束


        //统计币数  开始 币数= remaining_qty * 面值 / order_price
        Map<Integer, Map<Integer, Map<BigDecimal, BigDecimal>>> coinMap = list.stream().
                collect(Collectors.groupingBy(QuanOrderFuture::getSide,
                        Collectors.groupingBy(QuanOrderFuture::getOffset,
                                Collectors.groupingBy(QuanOrderFuture::getOrderPrice,
                                        CollectorsUtil.summingBigDecimal(QuanOrderFuture::getRemainingQty)))));

        List<QuanExchangeConfig> exchangeConfigs = quanExchangeConfigMapper.selectAll();
        exchangeConfigs.parallelStream().filter(e -> ExchangeEnum.HUOBI_FUTURE.getExId() == e.getExchangeId()).forEach(e -> {
            configMap.put(e.getExchangeId() + e.getBaseCoin() + e.getQuoteCoin(), e);
        });
        QuanExchangeConfig quanExchangeConfig = configMap.get(strategyInstanceConfig.getFutureExchangeId() + strategyInstanceConfig.getFutureBaseCoin() + strategyInstanceConfig.getFutureQuotCoin());
        BigDecimal faceValue = quanExchangeConfig.getFaceValue();
        Map<BigDecimal, BigDecimal> coinPrice11 = coinMap.get(1).get(1);//买入开多
        Map<BigDecimal, BigDecimal> coinPrice12 = coinMap.get(1).get(2);//买入平空
        Map<BigDecimal, BigDecimal> coinPrice21 = coinMap.get(2).get(1);//卖入开多
        Map<BigDecimal, BigDecimal> coinPrice22 = coinMap.get(2).get(2);//卖入开多

        buyOpenCoinNum = getCoinNum(coinPrice11, faceValue);
        buyCloseCoinNum = getCoinNum(coinPrice12, faceValue);
        sellOpenCoinNum = getCoinNum(coinPrice21, faceValue);
        sellCloseCoinNum = getCoinNum(coinPrice22, faceValue);
        buyCoinNumTotal = buyOpenCoinNum.add(buyCloseCoinNum);
        sellCoinNumTotal = sellOpenCoinNum.add(sellCloseCoinNum);

        //统计币数  结束


        //封装返回值
        resultMap.put("buyOpenRemainingQty", buyOpenRemainingQty);
        resultMap.put("buyOpenCoinNum", buyOpenCoinNum);
        resultMap.put("buyOpenOrderTotal", buyOpenOrderTotal);
        resultMap.put("buyCloseRemainingQty", buyCloseRemainingQty);
        resultMap.put("buyCloseCoinNum", buyCloseCoinNum);
        resultMap.put("buyCloseOrderTotal", buyCloseOrderTotal);
        resultMap.put("buyRemainingQtyTotal", buyRemainingQtyTotal);
        resultMap.put("buyCoinNumTotal", buyCoinNumTotal);
        resultMap.put("buyOrderTotal", buyOrderTotal);


        resultMap.put("sellOpenRemainingQty", sellOpenRemainingQty);
        resultMap.put("sellOpenCoinNum", sellOpenCoinNum);
        resultMap.put("sellOpenOrderTotal", sellOpenOrderTotal);
        resultMap.put("sellCloseRemainingQty", sellCloseRemainingQty);
        resultMap.put("sellCloseCoinNum", sellCloseCoinNum);
        resultMap.put("sellCloseOrderTotal", sellCloseOrderTotal);
        resultMap.put("sellRemainingQtyTotal", sellRemainingQtyTotal);
        resultMap.put("sellCoinNumTotal", sellCoinNumTotal);
        resultMap.put("sellOrderTotal", sellOrderTotal);

        return resultMap;
    }


    //key为 价格  value为该价格对应的张数
    //最终返回的是指定币种的币数
    // 币数= remaining_qty * 面值 / order_price
    private BigDecimal getCoinNum(Map<BigDecimal, BigDecimal> map, BigDecimal faceValue) {
        BigDecimal coinNum = BigDecimal.ZERO;
        for (Map.Entry<BigDecimal, BigDecimal> entry : map.entrySet()) {//每个价位对应的数量
            BigDecimal temp = entry.getValue().multiply(faceValue).subtract(entry.getKey());
            coinNum = coinNum.add(temp);
        }
        return coinNum;
    }


    @Override
    public Map<String, Object> findPriceData(Integer id) {
/*        SpotDepthReqDto spotDepthReqDto = new SpotDepthReqDto();
        StrategyInstanceConfig strategyInstanceConfig = strategyInstanceConfigMapper.selectByPrimaryKey(id);
        spotDepthReqDto.setExchangeId(strategyInstanceConfig.getSpotExchangeId());//交易所id
        spotDepthReqDto.setBaseCoin(strategyInstanceConfig.getFutureBaseCoin());
        spotDepthReqDto.setQuoteCoin(strategyInstanceConfig.getFutureQuotCoin());
        spotDepthReqDto.setDepthType("step0");

        //ServiceResult<SpotDepthRespDto> result = spotMarketService.getDepth(spotDepthReqDto);
        ServiceResult<SpotDepthRespDto> result = new ServiceResult<SpotDepthRespDto>();
        DepthBookInfo depthBook = new DepthBookInfo();
        if (result.isSuccess()) {
            SpotDepthRespDto.DataBean data = result.getData().getData();
            data.getAsks().forEach(e -> {
                depthBook.getAsks().add(new DepthBookInfo.Depth(e.getPrice(), e.getAmount()));
            });
            data.getBids().forEach(e -> {
                depthBook.getBids().add(new DepthBookInfo.Depth(e.getPrice(), e.getAmount()));
            });
        } else {
            throw new RuntimeException("获取火币现货深度异常");
        }
        BigDecimal ask1 = depthBook.getAsk1();
        BigDecimal bid1 = depthBook.getBid1();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("ask1", ask1);
        resultMap.put("bid1", bid1);*/
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("ask1", 12);
        resultMap.put("bid1", 14);
        return resultMap;
    }
}
