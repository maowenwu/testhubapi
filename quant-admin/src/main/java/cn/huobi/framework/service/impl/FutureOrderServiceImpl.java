package cn.huobi.framework.service.impl;

import cn.huobi.framework.service.FutureOrderService;
import com.huobi.quantification.dao.QuanOrderFutureMapper;
import com.huobi.quantification.entity.QuanOrderFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FutureOrderServiceImpl implements FutureOrderService {


    @Autowired
    QuanOrderFutureMapper quanOrderFutureMapper;


    @Override
    public List<QuanOrderFuture> selectUnfinishedByInstanceId(Long instanceId){
        List<QuanOrderFuture> list = quanOrderFutureMapper.selectUnfinishList();
        return list;
    }
}
