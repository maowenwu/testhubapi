package com.huobi.quantification.service.account;

import java.util.List;

import com.huobi.quantification.entity.QuanAccount;
import com.huobi.quantification.entity.QuanAccountAsset;
import com.huobi.quantification.entity.QuanAccountSecret;

/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public interface SpotAccountService {


    void updateAccount(Long accountId);

    List<QuanAccount> selectByExId(int exId);

    List<QuanAccountSecret> selectSecretById(Long accountId);
}
