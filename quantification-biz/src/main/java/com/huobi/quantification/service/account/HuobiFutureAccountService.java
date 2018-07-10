package com.huobi.quantification.service.account;

import com.huobi.quantification.response.future.HuobiFutureAccountInfoResponse;
import com.huobi.quantification.response.future.HuobiFuturePositionResponse;

public interface HuobiFutureAccountService {

    HuobiFutureAccountInfoResponse queryAccountInfoByAPI(String symbol);

    HuobiFuturePositionResponse queryPositionByAPI(String symbol);
}
