package cn.huobi.framework.service.sysUser;

import cn.huobi.framework.dto.StrategyInstanceInfo;
import com.huobi.quantification.entity.StrategyInstanceConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CommonService {

    //根据 futureBaseCoin  查询实例信息    --得到的是一组实例
    List<StrategyInstanceInfo> findStrategyInstanceInfo(String futureBaseCoin);


    //根据策略主键id查询基本信息
    StrategyInstanceConfig findStrategyInstanceBaseInfo(Integer id);

    //查询账户信息  todo
    Map<String, Object> findAccountInfo(Integer id);

    //买卖盘订单数据  --根据实例主键id查询
    Map<String, BigDecimal> findOrderData(Integer id);

    //买价格数据
    Map<String, Object> findPriceData(Integer id);

    //摆盘程序所下订单的买卖盘深度  根据实例id查询
    //List findOrderDept(Long instanceId);


    //根据策略主键id查询策略统计信息
    Map<String, Object> strategyRiskProfitCount(Integer strategyId);

    //根据实例id查询实例统计信息
    Map<String, Object> instanceRiskProfitCount(Long instanceId);


}
