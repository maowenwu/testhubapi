package com.huobi.quantification.service.account;

import java.util.List;

import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountSecret;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface HuobiAccountService {


    void updateAccount(Long accountId);

    List<Long> findAccountByExchangeId(int exId);

    List<QuanAccountSecret> findAccountSecretById(Long accountId);
}
