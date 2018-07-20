package com.huobi.quantification.service.account;

import java.util.List;

import com.huobi.quantification.entity.QuanAccountFutureSecret;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OkFutureAccountService {


    void updateOkUserInfo(Long accountId);

    void updateOkPosition(Long accountId);

    List<Long> findAccountFutureByExchangeId(int exchangeId);

    List<QuanAccountFutureSecret> findAccountFutureSecretById(Long id);
}
