package cn.huobi.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.BossOperLog;

/**
 * BossOperLog-DAO
 * @author YeXiaoMing
 * @date 2016年12月13日上午11:25:31
 */
public interface BossOperLogDao {
	
	//添加
    @Insert("insert into boss_oper_log(user_id,user_name,oper_code,request_method,request_params,"
    		+ "return_result, method_desc,oper_ip,oper_status,oper_time) "
    		+ "values (#{bossOperLog.user_id}, #{bossOperLog.user_name}, #{bossOperLog.oper_code}, #{bossOperLog.request_method}, #{bossOperLog.request_params},"
    		+ "#{bossOperLog.return_result},#{bossOperLog.method_desc},#{bossOperLog.oper_ip},#{bossOperLog.oper_status}, now())")
	int insert( @Param("bossOperLog")BossOperLog bossOperLog);

    @SuppressWarnings("rawtypes")
	@SelectProvider(type=SqlProvider.class, method="queryByCondition")
	@Results(value ={
			@Result(id=true, property="id",column="id",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="user_id",column="user_id",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="user_name",column="user_name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_code",column="oper_code",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="menu_name",column="menu_name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="request_method",column="request_method",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="return_result",column="return_result",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_ip",column="oper_ip",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_status",column="oper_status",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="oper_time",column="oper_time",javaType=Date.class,jdbcType=JdbcType.TIMESTAMP)
			})
	List<BossOperLog> queryByCondition(Page page, @Param("logInfo")BossOperLog logInfo);

	@Select("SELECT bol.*, CONCAT( IFNULL( concat(ppm.menu_name, '-'), '' ), pm.menu_name,"
		+"'-', m.menu_name ) menu_name FROM boss_oper_log bol LEFT JOIN boss_sys_menu m ON bol.oper_code = m.menu_code "
        +" LEFT JOIN boss_sys_menu pm ON m.parent_id = pm.id"
        +" LEFT JOIN boss_sys_menu ppm ON pm.parent_id = ppm.id"
        +" WHERE bol.id=#{id}")
	@Results(value ={
			@Result(id=true, property="id",column="id",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="user_id",column="user_id",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="user_name",column="user_name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_code",column="oper_code",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="menu_name",column="menu_name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="method_desc",column="method_desc",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="request_method",column="request_method",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="request_params",column="request_params",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="return_result",column="return_result",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_ip",column="oper_ip",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property="oper_status",column="oper_status",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property="oper_time",column="oper_time",javaType=Date.class,jdbcType=JdbcType.TIMESTAMP),
			})
	BossOperLog queryDetail(Integer id);

	public class SqlProvider{
		public String queryByCondition(Map<String, Object> param){
			BossOperLog logInfo = (BossOperLog) param.get("logInfo");
			StringBuffer sb = new StringBuffer("select bol.*,CONCAT( IFNULL( concat(ppm.menu_name, '-'), '' ), pm.menu_name,"
		+"'-', m.menu_name ) menu_name FROM boss_oper_log bol LEFT JOIN boss_sys_menu m ON bol.oper_code = m.menu_code "
        +" LEFT JOIN boss_sys_menu pm ON m.parent_id = pm.id"
        +" LEFT JOIN boss_sys_menu ppm ON pm.parent_id = ppm.id where 1=1 ");
			if(logInfo!=null && StringUtils.isNoneBlank(logInfo.getUser_name())){
				sb.append(" and bol.user_name like concat( #{logInfo.user_name},'%')");
			}
//			if(logInfo!=null && StringUtils.isNoneBlank(logInfo.getOper_code())){
//				sb.append(" and bol.oper_code like concat( #{logInfo.oper_code},'%')");
//			}
			if(logInfo!=null && logInfo.getOper_id()!=0){
				sb.append(" and (m.id=#{logInfo.oper_id})");
			} else if(logInfo!=null && logInfo.getMenu_id()!=0){
				sb.append(" and (m.id=#{logInfo.menu_id} or pm.id=#{logInfo.menu_id})");
			}else if(logInfo!=null && logInfo.getParent_menu_id()!=0){
				sb.append(" and (pm.id=#{logInfo.parent_menu_id} or ppm.id=#{logInfo.parent_menu_id} )");
			}
			if(logInfo!=null && logInfo.getOper_start_time()!=null){
				sb.append(" and bol.oper_time >= #{logInfo.oper_start_time}");
			}
			if(logInfo!=null && logInfo.getOper_end_time()!=null){
				sb.append(" and bol.oper_time <= #{logInfo.oper_end_time}");
			}
			sb.append(" order by bol.oper_time desc");
			System.out.println(sb.toString());
			return sb.toString();
		}
	}
}
