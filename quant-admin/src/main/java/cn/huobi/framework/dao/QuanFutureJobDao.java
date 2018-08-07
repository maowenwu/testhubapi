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

import com.huobi.quantification.entity.QuanJobFuture;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureJob;

public interface QuanFutureJobDao {

	@SelectProvider(type=SqlProvider.class, method="selectDicByCondition")
	@ResultType(QuanJobFuture.class)
	List<QuanJobFuture> selectDicByCondition(@Param("quanJob")QuanJobFuture quanJob, Page<FutureJob> page);
	
	@Delete("delete from quan_job_future where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Update("update quan_job_future set state = #{quanJob.state}, update_date = #{quanJob.updateDate} where id = #{quanJob.id}")
	int update(@Param("quanJob")QuanJobFuture quanJob);
	
	public class SqlProvider{
		
		public String selectDicByCondition(Map<String, Object> param){
			final QuanJobFuture quanJob = (QuanJobFuture) param.get("quanJob");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_job_future");
					if(quanJob!=null && quanJob.getExchangeId() != null){
						WHERE("exchange_id = #{quanJob.exchangeId}");
					}
					if(quanJob!=null && StringUtils.isNotBlank(quanJob.getJobDesc())){
						quanJob.setJobDesc(quanJob.getJobDesc()+"%");
						WHERE("job_desc like #{quanJob.jobDesc}");
					}
				}
			}.toString();
		}
	}

}
