package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.StrategyOrderConfig;

import cn.huobi.framework.db.pagination.Page;

public interface OrderDao {
	@SelectProvider(type=SqlProvider.class, method="selectDicByCondition")
	@ResultType(StrategyOrderConfig.class)
	List<StrategyOrderConfig> selectByCondition(@Param("config")StrategyOrderConfig job, Page<StrategyOrderConfig> page);
	
	@Delete("delete from strategy_order_config where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update strategy_order_config set contract_fee = #{config.contractFee}, spot_fee = #{config.spotFee}, delivery_fee = #{config.deliveryFee}, "
			+ "expect_yields = #{config.expectYields}, price_step = #{config.priceStep}, asks_max_amount = #{config.asksMaxAmount}, "
			+ "bids_max_amount = #{config.bidsMaxAmount}, asks_basis_price = #{config.asksBasisPrice}, bids_basis_price = #{config.bidsBasisPrice}, "
			+ "long_max_amount = #{config.longMaxAmount}, short_max_amount = #{config.shortMaxAmount} ,max_position_amount = #{config.maxPositionAmount}, "
			+ "min_position_amount = #{config.minPositionAmount}, contract_margin_reserve = #{config.contractMarginReserve}, "
			+ "spot_coin_reserve = #{config.spotCoinReserve}, spot_balance_reserve = #{config.spotBalanceReserve} "
			+ " where id = #{config.id}")
	int update(@Param("config")StrategyOrderConfig config);
	
	public class SqlProvider{
		
		public String selectDicByCondition(Map<String, Object> param){
			final StrategyOrderConfig config = (StrategyOrderConfig) param.get("config");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_order_config");
//					if(job!=null && job.getExchangeId() != null){
//						WHERE("exchange_id = #{job.exchangeId}");
//					}
//					if(job!=null && StringUtils.isNotBlank(job.getJobDesc())){
//						job.setJobDesc(job.getJobDesc()+"%");
//						WHERE("job_desc like #{job.jobDesc}");
//					}
				}
			}.toString();
		}
	}
}
