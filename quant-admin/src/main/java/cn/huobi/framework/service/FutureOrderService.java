package cn.huobi.framework.service;

import com.huobi.quantification.entity.QuanOrderFuture;

import java.util.List;

public interface FutureOrderService {

    // 查询指定实例（instance_id）下未完成和部分完成的订单
    List<QuanOrderFuture> selectUnfinishedByInstanceId(Long instanceId);


}
