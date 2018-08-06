package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.StrategyFinanceHistory;

import cn.huobi.framework.db.pagination.Page;

public interface FinanceDao {
	@SelectProvider(type=SqlProvider.class, method="selectDicByCondition")
	@ResultType(StrategyFinanceHistory.class)
	List<StrategyFinanceHistory> selectByCondition(@Param("config")StrategyFinanceHistory config, Page<StrategyFinanceHistory> page);
	
	@Delete("delete from strategy_finance_history where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("insert into strategy_finance_history set exchange_id = #{config.exchangeId}, account_id = #{config.accountId}, coin_type = #{config.coinType} , "
			+ "transfer_amount = #{config.transferAmount} , money_type = #{config.moneyType} , init = #{config.init} , "
			+ "create_time = #{config.createTime} , update_time = #{config.updateTime}")
	int insert(@Param("config")StrategyFinanceHistory config);
	
	public class SqlProvider{
		
		public String selectDicByCondition(Map<String, Object> param){
			final StrategyFinanceHistory config = (StrategyFinanceHistory) param.get("config");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_finance_history");
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
