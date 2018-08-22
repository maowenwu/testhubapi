package com.huobi.quantification.service.account;

import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;
import com.huobi.quantification.response.future.HuobiFutureAccountResponse;

import java.util.List;

public interface FutureAccountService {

    HuobiFutureAccountResponse queryAccountByAPI(Long accountId);

    HuobiFuturePositionResponse queryPositionByAPI(Long accountId);

    void updateHuobiAccount(Long accountId);

    void updateHuobiPosition(Long accountId);

    List<QuanAccountFuture> selectByExId(int exId);

    List<QuanAccountFutureSecret> selectSecretById(Long id);

    void initFutureAccountAsset();
}
