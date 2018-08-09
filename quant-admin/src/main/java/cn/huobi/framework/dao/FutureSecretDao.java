package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import com.huobi.quantification.entity.QuanAccountFutureSecret;

import cn.huobi.framework.db.pagination.Page;

public interface FutureSecretDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(QuanAccountFutureSecret.class)
	List<QuanAccountFutureSecret> selectByCondition(@Param("secret")QuanAccountFutureSecret secret, Page<QuanAccountFutureSecret> page);
	
	@Delete("delete from quan_account_future_secret where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Insert("insert into quan_account_future_secret(account_source_id, access_key, secret_key) "
			+ " values(#{secret.accountSourceId}, #{secret.accessKey}, #{secret.secretKey})")
	int insert(@Param("secret")QuanAccountFutureSecret secret);
	
	@Update("update quan_account_future_secret set account_source_id = #{secret.accountSourceId}, access_key = #{secret.accessKey}, secret_key = #{secret.secretKey} "
			+ "where id = #{secret.id}")
	int update(@Param("secret")QuanAccountFutureSecret secret);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final QuanAccountFutureSecret secret = (QuanAccountFutureSecret) param.get("secret");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_account_future_secret");
					if(secret!=null && secret.getAccountSourceId() != null){
						WHERE("account_source_id = #{secret.accountSourceId}");
					}
				}
			}.toString();
		}
	}

	
}
