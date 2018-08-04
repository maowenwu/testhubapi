package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.StrategyRiskConfig;

import cn.huobi.framework.db.pagination.Page;

public interface RiskDao {
	@SelectProvider(type=SqlProvider.class, method="selectDicByCondition")
	@ResultType(StrategyRiskConfig.class)
	List<StrategyRiskConfig> selectDicByCondition(@Param("config")StrategyRiskConfig job, Page<StrategyRiskConfig> page);
	
	@Delete("delete from strategy_risk_config where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update strategy_risk_config set risk_rate_level1 = #{config.riskRateLevel1}, risk_rate_level2 = #{config.riskRateLevel2}, "
			+ "risk_rate_level3 = #{config.riskRateLevel3}, net_position_level1 =#{config.netPositionLevel1}, net_position_level2 =#{config.netPositionLevel2}, "
			+ "curr_profit_level1 = #{config.currProfitLevel1}, curr_profit_level2 = #{config.currProfitLevel2}, "
			+ "total_profit_level1 = #{config.totalProfitLevel1}, total_profit_level2 = #{config.totalProfitLevel2} "
			+ " where id = #{config.id}")
	int update(@Param("config")StrategyRiskConfig config);
	
	public class SqlProvider{
		
		public String selectDicByCondition(Map<String, Object> param){
			final StrategyRiskConfig config = (StrategyRiskConfig) param.get("config");
			return new SQL(){
				{
					SELECT("*");
					FROM("strategy_risk_config");
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
