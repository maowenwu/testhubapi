package cn.huobi.framework.service;

import cn.huobi.framework.model.FutureAccount;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;

public interface FutureAccountDetailService {

    PageInfo<QuanAccountFutureAsset> selectAccountDetail(QuanAccountFuture quanAccountFuture, PageInfo<QuanAccountFutureAsset> page);

    PageInfo<QuanAccountFuturePosition> selectAccountPosition(QuanAccountFuturePosition quanAccountFuturePosition, PageInfo<QuanAccountFuturePosition> page);
}
