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

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.FutureAccount;
import cn.huobi.framework.util.StringUtil;

public interface FutureAccountDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(FutureAccount.class)
	List<FutureAccount> selectByCondition(@Param("account")FutureAccount account, Page<FutureAccount> page);
	
	@Delete("delete from quan_account_future where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Insert("insert into quan_account_future(exchange_id,account_source_id,accounts_type,accounts_name,state) values(#{account.exchangeId}, #{account.accountSourceId}, #{account.accountsType},"
			+ " #{account.accountsName}, #{account.state})")
	int insert(@Param("account")FutureAccount account);
	
	@Update("update quan_account_future set exchange_id = #{account.exchangeId}, account_source_id = #{account.accountSourceId}, "
			+ "accounts_type = #{account.accountsType}, accounts_name = #{account.accountsName} where id = #{account.id}")
	int update(@Param("account")FutureAccount account);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final FutureAccount account = (FutureAccount) param.get("account");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_account_future");
					if(account!=null && account.getExchangeId() != null){
						WHERE("exchange_id = #{account.exchangeId}");
					}
					if (account!=null && !StringUtil.isBlank(account.getAccountsName())) {
						account.setAccountsName(account.getAccountsName() + "%");
						WHERE("accounts_name like #{account.accountsName}");
					}
				}
			}.toString();
		}
	}

	
}
