package com.huobi.quantification.service.account;

import com.huobi.quantification.entity.QuanAccountFutureSecret;

import java.util.List;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface AccountService {

    void storeAllOkUserInfo();

    void storeAllOkPosition();

    List<Long> findAccountFutureByExchangeId(int exchangeId);

    List<QuanAccountFutureSecret> findAccountFutureSecretById(Long id);
}
