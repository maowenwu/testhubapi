package cn.huobi.framework.service.sysUser;

import cn.huobi.framework.dto.StrategyInstanceInfo;

import java.util.List;
import java.util.Map;

public interface CommonService {

    //根据 futureBaseCoin  查询实例信息    --得到的是一组实例
    List<StrategyInstanceInfo> findStrategyInstanceInfo(String futureBaseCoin);

    //查询账户信息  todo
    Map<String, Object> findAccountInfo();


}
