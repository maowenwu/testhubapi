package com.huobi.quantification.service.account;

public interface HuobiFutureAccountService {

    String queryUserInfoByAPI(Long accountId);

    String queryPositionByAPI(Long accountId);

    void updateHuobiUserInfo(Long accountId);

    void updateHuobiPosition(Long accountId);
    

}
