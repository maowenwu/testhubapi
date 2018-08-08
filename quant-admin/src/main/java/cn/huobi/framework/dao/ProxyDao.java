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

import com.huobi.quantification.entity.QuanProxyIp;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.ProxyIp;
import cn.huobi.framework.util.StringUtil;

public interface ProxyDao {
	@SelectProvider(type=SqlProvider.class, method="selectByCondition")
	@ResultType(QuanProxyIp.class)
	List<QuanProxyIp> selectByCondition(@Param("proxy")QuanProxyIp proxy, Page<ProxyIp> page);
	
	@Delete("delete from quan_proxy_ip where id=#{id}")
	int delete(@Param("id")Integer id);
	
	@Insert("insert into quan_proxy_ip(host,port,user_name,password,state,update_time,create_time)"
			+ " values(#{proxy.host}, #{proxy.port}, #{proxy.userName}, #{proxy.password}, "
			+ " #{proxy.state}, #{proxy.updateTime}, #{proxy.createTime})")
	int insert(@Param("proxy")QuanProxyIp proxy);
	
	@Update("update quan_proxy_ip set host = #{proxy.host}, port =  #{proxy.port}, user_name = #{proxy.userName} , "
			+ "password = #{proxy.password}, state = #{proxy.state}, update_time = #{proxy.updateTime} where id = #{proxy.id}")
	int update(@Param("proxy")QuanProxyIp proxy);
	
	public class SqlProvider{
		
		public String selectByCondition(Map<String, Object> param){
			final QuanProxyIp proxy = (QuanProxyIp) param.get("proxy");
			return new SQL(){
				{
					SELECT("*");
					FROM("quan_proxy_ip");
//					if(account!=null && account.getExchangeId() != null){
//						WHERE("exchange_id = #{account.exchangeId}");
//					}
					if (proxy!=null && !StringUtil.isBlank(proxy.getHost())) {
						proxy.setHost("%" + proxy.getHost() + "%");
						WHERE("host like #{proxy.host}");
					}
				}
			}.toString();
		}
	}

	
}
