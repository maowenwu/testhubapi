package com.huobi.quantification.dto;

import java.io.Serializable;

public class FutureQueryOrderStatusReqDto implements Serializable {

    private int exchangeId;
    private long accountId;
    private int status;
    private String baseCoin;
    private String quoteCoin;
    private String contractType;
    private String contractCode;
    private long maxDelay;


}
