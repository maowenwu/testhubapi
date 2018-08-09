package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.StrategyHedgeConfig;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.HedgeConfig;

public interface HadgeDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(StrategyHedgeConfig.class)
	List<StrategyHedgeConfig> selectByCondition(@Param("config")StrategyHedgeConfig config, Page<HedgeConfig> page);
	
	@Delete("delete from strategy_hedge_config where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update strategy_hedge_config set hedge_interval = #{config.hedgeInterval}, buy_slippage = #{config.buySlippage}, "
			+ "sell_slippage = #{config.sellSlippage}, stop_time1 = #{config.stopTime1}, stop_time2 = #{config.stopTime2} , delivery_interval=#{config.deliveryInterval}, "
			+ "delivery_buy_slippage = #{config.deliveryBuySlippage}, delivery_sell_slippage = #{config.deliverySellSlippage}, update_time = #{config.updateTime}"
			+ "where id = #{config.id}")
	int update(@Param("config")StrategyHedgeConfig config);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final StrategyHedgeConfig config = (StrategyHedgeConfig) param.get("config");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_hedge_config");
					if(config!=null && StringUtils.isNotBlank(config.getSymbol())){
						config.setSymbol("%"+ config.getSymbol() +"%");
						WHERE("symbol like #{config.symbol}");
					}
					if(config!=null && StringUtils.isNotBlank(config.getContractType())){
						config.setContractType("%"+ config.getContractType() +"%");
						WHERE("contract_type like #{config.contractType}");
					}
				}
			}.toString();
		}
	}
}
