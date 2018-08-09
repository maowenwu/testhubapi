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

import com.huobi.quantification.entity.StrategyTradeFee;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.TradeFee;

public interface FeeDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(StrategyTradeFee.class)
	List<StrategyTradeFee> selectByCondition(@Param("fee")StrategyTradeFee fee, Page<TradeFee> page);
	
	@Delete("delete from strategy_trade_fee where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update strategy_trade_fee set spot_fee = #{fee.spotFee}, contract_fee = #{fee.contractFee} , delivery_fee = #{fee.deliveryFee},"
			+ "update_time = #{fee.updateTime} where id = #{fee.id}")
	int update(@Param("fee")StrategyTradeFee fee);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final StrategyTradeFee fee = (StrategyTradeFee) param.get("fee");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_trade_fee");
					if(fee!=null && StringUtils.isNotBlank(fee.getSymbol())){
						fee.setSymbol("%"+ fee.getSymbol() +"%");
						WHERE("symbol like #{fee.symbol}");
					}
					if(fee!=null && StringUtils.isNotBlank(fee.getContractType())){
						fee.setContractType("%"+ fee.getContractType() +"%");
						WHERE("contract_type like #{fee.contractType}");
					}
				}
			}.toString();
		}
	}
}
