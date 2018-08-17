package cn.huobi.framework.dao;

import cn.huobi.framework.db.pagination.Page;
import com.huobi.quantification.entity.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface SysDictDao {

	@SelectProvider(type=SqlProvider.class, method="selectDicByCondition")
	@ResultType(SysDict.class)
	List<SysDict> selectDicByCondition(@Param("dict")SysDict dict, Page<SysDict> page);
	
	@Select("select * from sys_dict where status=1 and sys_key=#{key}")
	@ResultType(SysDict.class)
	SysDict getByKey(String key);
	
	@SelectProvider(type=SqlProvider.class, method="checkUnique")
	int checkUnique(@Param("sysDict")SysDict sysDict);
	
	@Select("SELECT sd.id,sd.sys_key,sd.sys_name,sd.sys_value,ag.group_no,ag.relation_type FROM `sys_dict` sd"
			+" LEFT JOIN activity_group ag on ag.activity_type=sd.sys_value"
			+" where sd.sys_key=#{key}"
			+" and sd.parent_id=#{key}"
			+" ORDER BY ag.group_no,sd.order_no")
	@ResultType(Map.class)
	List<Map<String, String>> getListByKey(@Param("key")String key);
	
	@Select("select sd.* from sys_dict sd "
			+ " where sd.status=1 and sd.parent_id=#{key} and sd.sys_key=#{key}")
	@ResultType(SysDict.class)
	List<SysDict> selectListByKey(@Param("key")String key);
	
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
	
	@Select("select * from sys_dict where sys_value=#{serviceType} and parent_id=#{acqServiceType}")
	@ResultType(SysDict.class)
	SysDict selectExistServiceLink(@Param("serviceType")String serviceType, @Param("acqServiceType")String acqServiceType);
	
	@Select("select sys_key,sys_name,sys_value from sys_dict where sys_key=#{key} and parent_id=#{key}")
	@ResultType(SysDict.class)
	List<SysDict> selectByKey(@Param("key")String key);

	@Update("update sys_dict set sys_value=#{sysValue} where parent_id='BOSS_UNIQUE' and sys_key=#{sysKey}")
	int updateSysValue(SysDict sysDict);
	
	@Update("update sys_dict set sys_value=#{sysValue} where parent_id='BOSS_UNIQUE' and sys_key=#{sysKey}")
	int updateValueByKey(@Param("sysValue")String sysValue, @Param("sysKey")String sysKey);
	
	@Select("select sys_value from sys_dict where sys_key=#{sysKey}")
	String getValueByKey(@Param("sysKey")String sysKey);
	
	@Select("select sys_value from sys_dict where parent_id=#{parentId} and status=1")
	List<String> getValueByparent(@Param("parentId")String parentId);


	/**
	 * 获取收单商户类别
	 * @param key
	 * @return
	 */
	@Select("select sd.id,sd.sys_value,sd.sys_name from sys_dict sd "
			+ " where sd.status=1 and sys_value!='0' and sd.parent_id=#{key} and sd.sys_key=#{key}")
	@ResultType(SysDict.class)
	List<SysDict> getAcqMerchantList(@Param("key")String key);

	public class SqlProvider{
		
		public String selectDicByCondition(Map<String, Object> param){
			final SysDict dict = (SysDict) param.get("dict");
			return new SQL(){
				{
					SELECT("*");
					FROM("sys_dict");
					if(dict!=null && StringUtils.isNotBlank(dict.getSysKey())){
						WHERE("upper(sys_key) like concat(upper(#{dict.sysKey}),'%')");
					}
					if(dict!=null && StringUtils.isNotBlank(dict.getSysName())){
						dict.setSysName(dict.getSysName()+"%");
						WHERE("sys_name like #{dict.sysName}");
					}
					if(dict!=null && dict.getStatus()!=2){
						WHERE("status = #{dict.status}");
					}
				}
			}.toString();
		}
		
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
