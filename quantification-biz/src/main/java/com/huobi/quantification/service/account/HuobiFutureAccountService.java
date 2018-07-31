package com.huobi.quantification.service.account;

import java.util.List;

public interface HuobiFutureAccountService {

    String queryUserInfoByAPI(Long accountId);

    String queryPositionByAPI(Long accountId);

    void updateHuobiUserInfo(Long accountId);

    void updateHuobiPosition(Long accountId);
    
    void saveFutureAccountsInfo(List<Long> accountIds);
}
