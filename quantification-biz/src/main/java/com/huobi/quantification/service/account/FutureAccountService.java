package com.huobi.quantification.service.account;

import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureSecret;

import java.util.List;

public interface FutureAccountService {

    String queryUserInfoByAPI(Long accountId);

    String queryPositionByAPI(Long accountId);

    void updateHuobiAccount(Long accountId);

    void updateHuobiPosition(Long accountId);

    List<QuanAccountFuture> selectByExId(int exId);

    List<QuanAccountFutureSecret> selectSecretById(Long id);
}
