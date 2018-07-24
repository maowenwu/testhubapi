package com.huobi.quantification.provider;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.future.FutureAccountService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.common.util.DateUtils;
import com.huobi.quantification.common.util.ThreadUtils;
import com.huobi.quantification.dto.FutureBalanceReqDto;
import com.huobi.quantification.dto.FutureBalanceRespDto;
import com.huobi.quantification.dto.FuturePositionReqDto;
import com.huobi.quantification.dto.FuturePositionRespDto;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.response.future.HuobiFutureUserInfoResponse;
import com.huobi.quantification.response.future.OKFuturePositionResponse;
import com.huobi.quantification.response.future.OKFutureUserinfoResponse;
import com.huobi.quantification.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class FutureAccountServiceImpl implements FutureAccountService {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public ServiceResult<FutureBalanceRespDto> getBalance(FutureBalanceReqDto reqDto) {
        ServiceResult<FutureBalanceRespDto> serviceResult = null;
        try {
            FutureBalanceRespDto balanceRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新资产
                    QuanAccountFutureAsset futureAsset = redisService.getFutureUserInfo(reqDto.getExchangeId(), reqDto.getAccountId());
                    if (futureAsset == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = futureAsset.getUpdateTime();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        return parseBalanceResp(reqDto.getExchangeId(), reqDto.getCoinType(), futureAsset);
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(balanceRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    private FutureBalanceRespDto parseBalanceResp(int exchangeId, String coinType, QuanAccountFutureAsset futureAsset) {
        if (exchangeId == ExchangeEnum.OKEX.getExId()) {
            return parseOkFutureBalanceResp(coinType, futureAsset);
        } else if (exchangeId == ExchangeEnum.HUOBI_FUTURE.getExId()) {
            return parseHuobiFutureBalanceResp(coinType, futureAsset);
        }
        throw new UnsupportedOperationException("不支持该交易所" + exchangeId);
    }

    private FutureBalanceRespDto parseHuobiFutureBalanceResp(String coinType, QuanAccountFutureAsset futureAsset) {
        FutureBalanceRespDto respDto = new FutureBalanceRespDto();
        respDto.setTs(futureAsset.getUpdateTime());
        HuobiFutureUserInfoResponse userinfoResponse = JSON.parseObject(futureAsset.getRespBody(), HuobiFutureUserInfoResponse.class);
        Map<String, FutureBalanceRespDto.DataBean> data = new ConcurrentHashMap<>();
        List<HuobiFutureUserInfoResponse.DataBean> dataBeans = userinfoResponse.getData();
        for (HuobiFutureUserInfoResponse.DataBean dataBean : dataBeans) {
            data.put(dataBean.getSymbol(), convertToDto(dataBean));
        }
        if (StringUtils.isNotEmpty(coinType)) {
            data.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    data.remove(k);
                }
            });
        }
        respDto.setData(data);
        return respDto;
    }

    private FutureBalanceRespDto.DataBean convertToDto(HuobiFutureUserInfoResponse.DataBean dataBean) {
        FutureBalanceRespDto.DataBean respDto = new FutureBalanceRespDto.DataBean();
        respDto.setMarginBalance(dataBean.getMarginBalance());
        respDto.setProfitReal(dataBean.getProfitReal());
        respDto.setProfitUnreal(dataBean.getProfitUnreal());
        respDto.setRiskRate(dataBean.getRiskRate());
        // 持仓保证金
        respDto.setMarginPosition(dataBean.getMarginPosition());
        return respDto;
    }

    private FutureBalanceRespDto parseOkFutureBalanceResp(String coinType, QuanAccountFutureAsset futureAsset) {
        FutureBalanceRespDto respDto = new FutureBalanceRespDto();
        respDto.setTs(futureAsset.getUpdateTime());
        OKFutureUserinfoResponse userinfoResponse = JSON.parseObject(futureAsset.getRespBody(), OKFutureUserinfoResponse.class);

        OKFutureUserinfoResponse.InfoBean infoBean = userinfoResponse.getInfo();
        Map<String, FutureBalanceRespDto.DataBean> data = new ConcurrentHashMap<>();
        data.put("btc", convertToDto(infoBean.getBtc()));
        data.put("btg", convertToDto(infoBean.getBtg()));
        data.put("etc", convertToDto(infoBean.getEtc()));
        data.put("bch", convertToDto(infoBean.getBch()));
        data.put("xrp", convertToDto(infoBean.getXrp()));
        data.put("eth", convertToDto(infoBean.getEth()));
        data.put("eos", convertToDto(infoBean.getEos()));
        data.put("ltc", convertToDto(infoBean.getLtc()));
        if (StringUtils.isNotEmpty(coinType)) {
            data.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    data.remove(k);
                }
            });
        }
        respDto.setData(data);
        return respDto;
    }


    private FutureBalanceRespDto.DataBean convertToDto(OKFutureUserinfoResponse.DataBean dataBean) {
        FutureBalanceRespDto.DataBean respDto = new FutureBalanceRespDto.DataBean();
        respDto.setMarginBalance(dataBean.getAccountRights());
        // 持仓保证金
        respDto.setMarginPosition(dataBean.getKeepDeposit());
        //todo 未确认
        respDto.setMarginFrozen(null);
        //todo 未确认
        respDto.setMarginAvailable(null);
        respDto.setProfitReal(dataBean.getProfitReal());
        respDto.setProfitUnreal(dataBean.getProfitUnreal());
        respDto.setRiskRate(dataBean.getRiskRate());
        return respDto;
    }


    @Override
    public ServiceResult<FuturePositionRespDto> getPosition(FuturePositionReqDto reqDto) {
        ServiceResult<FuturePositionRespDto> serviceResult = null;
        try {
            FuturePositionRespDto positionRespDto = AsyncUtils.supplyAsync(() -> {
                while (!Thread.interrupted()) {
                    // 从redis读取最新资产
                    QuanAccountFuturePosition futurePosition = redisService.getFuturePosition(reqDto.getExchangeId(), reqDto.getAccountId());
                    if (futurePosition == null) {
                        ThreadUtils.sleep10();
                        continue;
                    }
                    Date ts = futurePosition.getUpdateTime();
                    if (DateUtils.withinMaxDelay(ts, reqDto.getMaxDelay())) {
                        return parsePositionResp(reqDto.getExchangeId(), reqDto.getCoinType(), futurePosition);
                    } else {
                        ThreadUtils.sleep10();
                        continue;
                    }
                }
                return null;
            }, reqDto.getTimeout());
            serviceResult = ServiceResult.buildSuccessResult(positionRespDto);
        } catch (ExecutionException e) {
            logger.error("执行异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.EXECUTION_ERROR);
        } catch (TimeoutException e) {
            logger.error("超时异常：", e);
            serviceResult = ServiceResult.buildErrorResult(ServiceErrorEnum.TIMEOUT_ERROR);
        }
        return serviceResult;
    }

    private FuturePositionRespDto parsePositionResp(int exchangeId, String coinType, QuanAccountFuturePosition position) {
        if (exchangeId == ExchangeEnum.OKEX.getExId()) {
            return parseOkFuturePositionResp(coinType, position);
        } else if (exchangeId == ExchangeEnum.HUOBI_FUTURE.getExId()) {
            return parseHuobiFuturePositionResp(coinType, position);
        }
        throw new UnsupportedOperationException("不支持该交易所" + exchangeId);

    }

    private FuturePositionRespDto parseHuobiFuturePositionResp(String coinType, QuanAccountFuturePosition position) {
        FuturePositionRespDto respDto = new FuturePositionRespDto();
        HuobiFuturePositionResponse response = JSON.parseObject(position.getRespBody(), HuobiFuturePositionResponse.class);
        List<FuturePositionRespDto.DataBean> beanList = new ArrayList<>();
        response.getData().forEach((e) -> {
            FuturePositionRespDto.DataBean dataBean = new FuturePositionRespDto.DataBean();
            dataBean.setContractCode(e.getContractCode());
            dataBean.setBaseCoin(null);
            dataBean.setQuoteCoin(null);
            dataBean.setContractType(e.getContractType());
            dataBean.setLongAmount(e.getVolume());
            dataBean.setLongAvailable(e.getAvailable());
            // 多仓冻结张数
            dataBean.setLongFrozen(e.getFrozen());
            dataBean.setLongCostOpen(e.getCostOpen());
            // 多仓持仓均价
            dataBean.setLongCostHold(e.getCostHold());
            dataBean.setLeverRate(e.getLeverRate());
            beanList.add(dataBean);
        });
        Map<String, List<FuturePositionRespDto.DataBean>> dataMap = beanList.stream().collect(Collectors.groupingByConcurrent((a) -> a.getBaseCoin().toLowerCase()));
        if (StringUtils.isNotEmpty(coinType)) {
            dataMap.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    dataMap.remove(k);
                }
            });
        }
        respDto.setData(dataMap);
        return respDto;

    }

    private FuturePositionRespDto parseOkFuturePositionResp(String coinType, QuanAccountFuturePosition position) {
        FuturePositionRespDto respDto = new FuturePositionRespDto();
        OKFuturePositionResponse response = JSON.parseObject(position.getRespBody(), OKFuturePositionResponse.class);
        List<FuturePositionRespDto.DataBean> beanList = new ArrayList<>();
        response.getHolding().forEach((e) -> {
            beanList.add(getLongPosition(e));
            beanList.add(getShortPosition(e));
        });
        Map<String, List<FuturePositionRespDto.DataBean>> dataMap = beanList.stream().collect(Collectors.groupingByConcurrent((a) -> a.getBaseCoin().toLowerCase()));
        if (StringUtils.isNotEmpty(coinType)) {
            dataMap.forEach((k, v) -> {
                if (!k.equalsIgnoreCase(coinType)) {
                    dataMap.remove(k);
                }
            });
        }
        respDto.setData(dataMap);
        return respDto;
    }

    // 获取多仓持仓
    private FuturePositionRespDto.DataBean getLongPosition(OKFuturePositionResponse.HoldingBean e) {
        FuturePositionRespDto.DataBean dataBean = new FuturePositionRespDto.DataBean();
        // 合约code
        // todo 这个需要在查询的时候塞进去？？
        dataBean.setContractCode(e.getContractId() + "");
        String symbol = e.getSymbol();
        String[] split = symbol.split("_");
        //
        dataBean.setBaseCoin(split[0]);
        //
        dataBean.setQuoteCoin(split[1]);
        // 合约类型
        dataBean.setContractType(e.getContractType());
        // 多仓张数
        dataBean.setLongAmount(e.getBuyAmount());
        // 多仓可平张数
        dataBean.setLongAvailable(e.getBuyAvailable());
        // 多仓冻结张数
        dataBean.setLongFrozen(e.getBuyAmount().subtract(e.getBuyAvailable()));
        // 空仓开仓均价
        dataBean.setLongCostOpen(e.getBuyPriceAvg());
        // 多仓持仓均价
        dataBean.setLongCostHold(e.getBuyPriceCost());
        // 杠杆倍数
        dataBean.setLeverRate(e.getLeverRate());
        return dataBean;
    }
    // 获取空仓持仓
    private FuturePositionRespDto.DataBean getShortPosition(OKFuturePositionResponse.HoldingBean e) {
        FuturePositionRespDto.DataBean dataBean = new FuturePositionRespDto.DataBean();
        // 合约code
        dataBean.setContractCode(e.getContractId() + "");
        String symbol = e.getSymbol();
        String[] split = symbol.split("_");
        //
        dataBean.setBaseCoin(split[0]);
        //
        dataBean.setQuoteCoin(split[1]);
        // 合约类型
        dataBean.setContractType(e.getContractType());
        // 空仓张数
        dataBean.setShortAmount(e.getSellAmount());
        // 空仓可平张数
        dataBean.setShortAvailable(e.getSellAvailable());
        // 空仓冻结张数
        dataBean.setShortFrozen(e.getSellAmount().subtract(e.getSellAvailable()));
        // 空仓开仓均价
        dataBean.setShortCostOpen(e.getSellPriceAvg());
        // 空仓持仓均价
        dataBean.setShortCostHold(e.getSellPriceCost());
        // 杠杆倍数
        dataBean.setLeverRate(e.getLeverRate());
        return dataBean;
    }
}
