package com.huobi.quantification.common.constant;

import java.util.HashMap;
import java.util.Map;

public class OkRestErrorCode {

    private static Map<Integer, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put(20001, "用户不存在");
        errorCodeMap.put(20002, "用户被冻结");
        errorCodeMap.put(20003, "用户被爆仓冻结");
        errorCodeMap.put(20004, "合约账户被冻结");
        errorCodeMap.put(20005, "用户合约账户不存在");
        errorCodeMap.put(20006, "必填参数为空");
        errorCodeMap.put(20007, "参数错误");
        errorCodeMap.put(20008, "合约账户余额为空");
        errorCodeMap.put(20009, "虚拟合约状态错误");
        errorCodeMap.put(20010, "合约风险率信息不存在");
        errorCodeMap.put(20011, "10倍/20倍杠杆开BTC前保证金率低于90%/80%，10倍/20倍杠杆开LTC前保证金率低于80%/60%");
        errorCodeMap.put(20012, "10倍/20倍杠杆开BTC后保证金率低于90%/80%，10倍/20倍杠杆开LTC后保证金率低于80%/60%");
        errorCodeMap.put(20013, "暂无对手价");
        errorCodeMap.put(20014, "系统错误");
        errorCodeMap.put(20015, "订单信息不存在");
        errorCodeMap.put(20016, "平仓数量是否大于同方向可用持仓数量");
        errorCodeMap.put(20017, "非本人操作");
        errorCodeMap.put(20018, "下单价格高于前一分钟的103%或低于97%");
        errorCodeMap.put(20019, "该IP限制不能请求该资源");
        errorCodeMap.put(20020, "密钥不存在");
        errorCodeMap.put(20021, "指数信息不存在");
        errorCodeMap.put(20022, "接口调用错误（全仓模式调用全仓接口，逐仓模式调用逐仓接口）");
        errorCodeMap.put(20023, "逐仓用户");
        errorCodeMap.put(20024, "sign签名不匹配");
        errorCodeMap.put(20025, "杠杆比率错误");
        errorCodeMap.put(20026, "API鉴权错误");
        errorCodeMap.put(20027, "无交易记录");
        errorCodeMap.put(20028, "合约不存在");
        errorCodeMap.put(20029, "转出金额大于可转金额");
        errorCodeMap.put(20030, "账户存在借款");
        errorCodeMap.put(20038, "根据相关法律，您所在的国家或地区不能使用该功能。");
        errorCodeMap.put(20049, "用户请求接口过于频繁");
        errorCodeMap.put(20061, "合约相同方向只支持一个杠杆，若有10倍多单，就不能再下20倍多单");
    }

    public static String findErrorMessageByCode(int errorCode) {
        String msg = errorCodeMap.get(errorCode);
        if (msg == null) {
            return "未知程序错误";
        }
        return msg;
    }
}
