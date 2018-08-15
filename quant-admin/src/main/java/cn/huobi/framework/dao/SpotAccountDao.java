package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.QuanSpotAccount;
import cn.huobi.framework.model.SpotAccount;

public interface SpotAccountDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(QuanSpotAccount.class)
	List<QuanSpotAccount> selectByCondition(@Param("account")QuanSpotAccount config, Page<SpotAccount> page);
	
	@Delete("delete from quan_account where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Insert("insert into quan_account(exchange_id,account_source_id,accounts_type,accounts_name,state) values(#{account.exchangeId}, #{account.accountSourceId}, #{account.accountsType},"
			+ " #{account.accountsName}, #{account.state})")
	int insert(@Param("account")QuanSpotAccount account);
	
	@Update("update quan_account set exchange_id = #{account.exchangeId}, account_source_id = #{account.accountSourceId}, "
			+ "accounts_type = #{account.accountsType}, accounts_name = #{account.accountsName} where id = #{account.id}")
	int update(@Param("account")QuanSpotAccount account);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final QuanSpotAccount account = (QuanSpotAccount) param.get("account");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_account");
					if(account!=null && account.getExchangeId() != null){
						WHERE("exchange_id = #{account.exchangeId}");
					}
					if (account!=null && !StringUtils.isBlank(account.getAccountsName())) {
						account.setAccountsName(account.getAccountsName() + "%");
						WHERE("accounts_name like #{account.accountsName}");
					}
				}
			}.toString();
		}
	}

	
}
