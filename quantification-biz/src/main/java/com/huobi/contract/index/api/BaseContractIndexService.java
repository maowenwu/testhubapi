package com.huobi.contract.index.api;

import com.huobi.contract.index.common.redis.RedisService;
import com.huobi.contract.index.dao.ContractPriceIndexMapper;
import com.huobi.contract.index.entity.ContractPriceIndex;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseContractIndexService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ContractPriceIndexMapper contractPriceIndexMapper;
    /**
     * 获取最新的合约指数价格信息
     * @param indexSymbol 交易币对
     * @return
     */
    public ContractPriceIndex getLastContractIndex(String indexSymbol){
        //先从redis中获取
        //从redis获取最新的价格信息
        ContractPriceIndex redisContractIndex = redisService.getLatestIndexPirce(indexSymbol);
        if(redisContractIndex==null){
            //从数据库中获取最新的价格信息
            ContractPriceIndex lastIndex = contractPriceIndexMapper.getLastIndex(indexSymbol);
            return lastIndex;
        }else{
            return redisContractIndex;
        }
    }
}
