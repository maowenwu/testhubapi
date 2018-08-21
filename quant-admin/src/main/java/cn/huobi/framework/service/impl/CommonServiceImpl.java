package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.FeeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.dto.StrategyInstanceInfo;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;
import cn.huobi.framework.service.sysUser.CommonService;
import com.huobi.quantification.dao.*;
import com.huobi.quantification.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


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


    @Override
    public List<StrategyInstanceInfo> findStrategyInstanceInfo(String futureBaseCoin) {
        List<StrategyInstanceInfo> resultList=new ArrayList<>();
        StrategyInstanceConfig strategyInstanceConfig=new StrategyInstanceConfig();
        strategyInstanceConfig.setFutureBaseCoin(futureBaseCoin);
        //获取到一组实例
        List<StrategyInstanceConfig> list=strategyInstanceConfigMapper.selectList(strategyInstanceConfig);
        //查询每一个实例总盈亏
        for(StrategyInstanceConfig temp:list){
            StrategyInstanceInfo strategyInstanceInfo=new StrategyInstanceInfo();
            StrategyRiskHistory entity=strategyRiskHistoryMapper.selectLatestByInstanceIdCoin(temp.getInstanceId(),temp.getFutureBaseCoin());
            //todo 运行状态要根据心跳判断
            if(null==entity){
                continue;
            }
            strategyInstanceInfo.setInstanceGroup(temp.getInstanceGroup());//账户组别
            strategyInstanceInfo.setInstanceEnable(temp.getInstanceEnable());//运行状态
            strategyInstanceInfo.setTotalProfit(entity.getTotalProfit());////总盈亏
            strategyInstanceInfo.setFutureContractCode(temp.getFutureContractCode());//合约代码
            resultList.add(strategyInstanceInfo);
        }
        return resultList;
    }

    //todo
    @Override
    public Map<String, Object> findAccountInfo() {
        Map<String, Object> map = new HashMap<>();
        Long spotAccountId = 1L;
        Long futureAccountSourceId = 1L;
        String spotQuoteCoin="USDT";
        String spotBaseCoin = "BTC";
        String futureBaseCoin = "BTC";

        QuanAccountAsset spotQuoteCoinAssert=quanAccountAssetMapper.selectLatestByAccountIdCoinType(spotAccountId, spotQuoteCoin);
        QuanAccountAsset spotBaseCoinAssert=quanAccountAssetMapper.selectLatestByAccountIdCoinType(spotAccountId, spotBaseCoin);
        QuanAccountFutureAsset futureBaseCoinAsset=quanAccountFutureAssetMapper.selectLatestByAccountSourceIdCoinType(futureAccountSourceId, futureBaseCoin);
        QuanAccountFuturePosition futureBaseCoinPosition=quanAccountFuturePositionMapper.selectLatestByAccountSourceIdBaseCoin(futureAccountSourceId, futureBaseCoin);

        map.put("spotQuoteCoinAmount",spotQuoteCoinAssert.getTotal());//持币量 （BTC）
        map.put("spotBaseCoinAmount",spotBaseCoinAssert.getTotal());//资金量（USDT）

        map.put("futurePositionAmount",futureBaseCoinPosition.getAmount());//持仓量（张）
        map.put("futureAssetAmount",futureBaseCoinAsset.getMarginBalance());//持币量 （BTC）

        return map;
    }
}
