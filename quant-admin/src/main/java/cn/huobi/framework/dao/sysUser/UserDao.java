package cn.huobi.framework.dao.sysUser;
//package cn.huobi.framework.dao.sysUser;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.ResultMap;
//import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.SelectKey;
//import org.apache.ibatis.annotations.SelectProvider;
//import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.jdbc.SQL;
//
//import cn.huobi.framework.db.pagination.Page;
//import cn.huobi.framework.db.pagination.Sort;
//import cn.huobi.framework.model.ShiroRole;
//import cn.huobi.framework.model.ShiroUser;
//
//public interface UserDao{
//	/**
//	 * 新增用户信息
//	 */
//	@Insert("insert into boss_shiro_user(user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time)"
//			+ " values(#{user.userName},#{user.password},#{user.realName},#{user.telNo},#{user.email},#{user.state},"
//			+ "#{user.theme},#{user.createOperator},#{user.createTime})")
//	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)  
//	int insert(@Param("user")ShiroUser shiroUser);
//	
//	@Update("update boss_shiro_user set user_name = #{shiroUser.userName},real_name= #{shiroUser.realName},tel_no= #{shiroUser.telNo},email= #{shiroUser.email} where id = #{shiroUser.id}")
//	int updateUser(@Param("shiroUser")ShiroUser shiroUser);
//	
//	@Update("update boss_shiro_user set password = #{password} where id = #{id}")
//	int updateUserPwd(@Param("id")Integer id,@Param("password")String password);
//	
//	@SelectProvider(type=SqlProvider.class,method="findUsers")
//	@ResultMap("cn.huobi.framework.dao.ShiroUserMapper.BaseResultMap")
//	List<ShiroUser> findUsers(@Param("user")ShiroUser user,@Param("sort")Sort sort,Page<ShiroUser> page);
//	
//	@Select("select * from boss_shiro_user")
//	@ResultMap("cn.huobi.framework.dao.ShiroUserMapper.OneToManyResultMap")
//	List<ShiroUser> findUsersWithRole();
//	
//	@Select("select * from boss_shiro_user where user_name =#{userName}")
//	@ResultMap("cn.huobi.framework.dao.ShiroUserMapper.OneToManyResultMap")
//	ShiroUser findUsersWithRoleByUserName(@Param("userName")String userName);
//	
//	@Select("select * from boss_shiro_user where id =#{id}")
//	@ResultMap("cn.huobi.framework.dao.ShiroUserMapper.BaseResultMap")
//	ShiroUser findUserById(@Param("id")Integer id);
//	
//	public class SqlProvider{
//		public String findUsers(Map<String,Object> param){
//			final ShiroUser user=(ShiroUser)param.get("user");
//			final Sort sord=(Sort)param.get("sort");
//			return new SQL(){{
//				SELECT("user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time");
//				FROM("boss_shiro_user");
//				if(StringUtils.isNotBlank(user.getUserName())){
//					WHERE("user_name=#{user.userName}");
//				}
//				if(StringUtils.isNotBlank(user.getRealName())){
//					WHERE("real_name=#{user.realName}");
//				}
//				if(StringUtils.isNotBlank(user.getEmail())){
//					WHERE("email=#{user.email}");
//				}
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
//		public String propertyMapping(String name,int type){
//			final String[] propertys={"userName","password","realName","telNo","email","state","theme",
//		    		"createOperator","createTime"};
//		    final String[] columns={"user_Name","password","real_Name","tel_No","email","state","theme",
//		    		"create_Operator","create_Time"};
//		    if(StringUtils.isNotBlank(name)){
//		    	if(type==0){//属性查出字段名
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(propertys[i])){
//		    				return columns[i];
//		    			}
//		    		}
//		    	}else if(type==1){//字段名查出属性
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(columns[i])){
//		    				return propertys[i];
//		    			}
//		    		}
//		    	}
//		    }
//			return null;
//		}
//	}
//}
