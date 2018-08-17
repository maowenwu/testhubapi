package cn.huobi.framework.dao;

import cn.huobi.framework.db.pagination.Page;
import com.huobi.quantification.entity.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface SysDictDao {


	@Select("select * from sys_dict where status=1 and sys_key=#{key}")
	@ResultType(SysDict.class)
	SysDict getByKey(String key);
	
	@SelectProvider(type=SqlProvider.class, method="checkUnique")
	int checkUnique(@Param("sysDict")SysDict sysDict);
	


	@Insert("insert into sys_dict (sys_key,sys_name,sys_value,parent_id,order_no,status,remark) values "
			+ "(#{info.sysKey},#{info.sysName},#{info.sysValue},#{info.parentId},#{info.orderNo},"
			+ "#{info.status},#{info.remark})")
	int insert(@Param("info")SysDict info);

	@Update("update sys_dict set sys_key=#{info.sysKey},sys_name=#{info.sysName},sys_value=#{info.sysValue},"
			+ "parent_id=#{info.parentId},order_no=#{info.orderNo},status=#{info.status},remark=#{info.remark} "
			+ "where id=#{info.id}")
	int update(@Param("info")SysDict info);

	@Delete("delete from sys_dict where id=#{id}")
	int delete(@Param("id")Integer id);

	@Select("SELECT s.*,p.sys_value `type` FROM sys_dict p LEFT JOIN sys_dict s  ON p.sys_key=s.parent_id WHERE s.status=1 and p.parent_id='BOSS_INIT' ORDER BY s.sys_key,s.order_no")
	@ResultType(SysDict.class)
	List<SysDict> selectAllDict();
	
	@Select("select * from sys_dict where status=1 and sys_key='INITIAL_PWD'")
	@ResultType(SysDict.class)
	SysDict selectRestPwd();
	

	@Update("update sys_dict set sys_value=#{sysValue} where parent_id='BOSS_UNIQUE' and sys_key=#{sysKey}")
	int updateSysValue(SysDict sysDict);
	




	public class SqlProvider{


		public String checkUnique(Map<String, Object> param){
			final SysDict sysDict = (SysDict) param.get("sysDict");
			return new SQL(){{
				SELECT("COUNT(1)");
				FROM("sys_dict");
				WHERE("sys_key=#{sysDict.sysKey}");
				if(sysDict!=null && sysDict.getId()!=null){
					WHERE("id<>#{sysDict.id}");
				}
			}}.toString();
		}
	}



}
