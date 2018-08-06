package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.huobi.framework.db.pagination.Page;

public interface HadgeDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(StrategyHedgingConfig.class)
	List<StrategyHedgingConfig> selectByCondition(@Param("config")StrategyHedgingConfig config, Page<StrategyHedgingConfig> page);
	
	@Delete("delete from strategy_hedge_config where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update strategy_hedge_config set formality_rate = #{config.formalityRate}, slippage = #{config.slippage} where id = #{config.id}")
	int update(@Param("config")StrategyHedgingConfig config);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final StrategyHedgingConfig config = (StrategyHedgingConfig) param.get("config");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_hedge_config");
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
