package cn.huobi.framework.service;

import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;

import java.util.List;

public interface FutureAccountDetailService {

    QuanAccountFuture selectAccountById(int id);

    List<QuanAccountFutureAsset> selectAccountDetail(Integer accountId);
}
