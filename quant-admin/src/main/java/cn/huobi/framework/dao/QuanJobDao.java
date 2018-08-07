package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.QuanJob;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SpotJob;
import cn.huobi.framework.util.StringUtil;

public interface QuanJobDao {

	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(QuanJob.class)
	List<QuanJob> selectByCondition(@Param("quanJob")QuanJob quanJob, Page<SpotJob> page);
	
	@Delete("delete from quan_job where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update quan_job set state = #{quanJob.state}, update_date = #{quanJob.updateDate} where id = #{quanJob.id}")
	int update(@Param("quanJob")QuanJob quanJob);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final QuanJob quanJob = (QuanJob) param.get("quanJob");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_job");
					if(quanJob!=null && quanJob.getExchangeId() != null){
						WHERE("exchange_id = #{quanJob.exchangeId}");
					}
					if(quanJob!=null && !StringUtil.isBlank(quanJob.getJobDesc())){
						quanJob.setJobDesc(quanJob.getJobDesc()+"%");
						WHERE("job_desc like #{quanJob.jobDesc}");
					}
				}
			}.toString();
		}
	}

}
