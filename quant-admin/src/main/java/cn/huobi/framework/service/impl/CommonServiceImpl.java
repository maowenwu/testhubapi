package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.FeeDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;
import cn.huobi.framework.service.FeeService;
import cn.huobi.framework.service.sysUser.CommonService;
import com.huobi.quantification.dao.QuanAccountAssetMapper;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import com.huobi.quantification.entity.StrategyTradeFee;
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
