package com.huobi.quantification.service.account;

import com.huobi.quantification.entity.QuanAccountFutureSecret;
import com.huobi.quantification.enums.OkContractType;

import java.util.List;

/**
 * @author zhangl
 * @since 2018/6/26
 */
public interface OkFutureAccountService {


    void updateOkUserInfo(Long accountId);

    void updateOkPosition(Long accountId, String symbol, String contractType);

    List<Long> findAccountFutureByExchangeId(int exchangeId);

    List<QuanAccountFutureSecret> findAccountFutureSecretById(Long id);
}
