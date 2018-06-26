package com.huobi.contract.index.ws;

import com.huobi.contract.index.contract.index.common.ExchangeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStatusManager {

    public static final Logger logger = LoggerFactory.getLogger(ConnectionStatusManager.class);
    private static ConcurrentHashMap<ExchangeEnum, ConcurrentHashMap<String, ConnectionStatus>> connMap = new ConcurrentHashMap<>();

    public static void register(ExchangeEnum exchangeEnum, String symbol, ConnectionStatus connObj) {
        ConcurrentHashMap<String, ConnectionStatus> symbolConnMap = connMap.get(exchangeEnum);
        if (symbolConnMap == null) {
            symbolConnMap = new ConcurrentHashMap<>();
            connMap.put(exchangeEnum, symbolConnMap);
        }
        symbolConnMap.put(symbol, connObj);
    }


    public static boolean wsReconnecting(ExchangeEnum exchangeEnum, String symbol) {
        return connMap.get(exchangeEnum).get(symbol).isReconnecting();
    }
}
