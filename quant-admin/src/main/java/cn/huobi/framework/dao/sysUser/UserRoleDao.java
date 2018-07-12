package cn.huobi.framework.dao.sysUser;
//package cn.huobi.framework.dao.sysUser;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.ResultMap;
//import org.apache.ibatis.annotations.Select;
//
//import cn.huobi.framework.model.UserRole;
///**
// * 
// * by zouruijin
// * email rjzou@qq.com zrj@eeepay.cn
// * 2016年4月12日13:45:54
// *
// */
//public interface UserRoleDao{
//	
//	
//	@Insert("insert into boss_user_role(user_id,role_id) values(#{userId},#{roleId})")
//	int insertUserRole(@Param("userId")Integer userId,@Param("roleId")Integer roleId);
//	
//	
//	@Delete("delete from boss_user_role where user_id = #{userId} and role_id = #{role_id}")
//	int deleteUserRole(@Param("userId")Integer userId,@Param("role_id")Integer role_id);
//	
//	@Delete("delete from boss_user_role where user_id = #{userId}")
//	int deleteUserRoleByUserId(@Param("userId")Integer userId);
//	
//	@Select(" select ur.id,ur.user_id,ur.role_id,sr.id as role_id,sr.role_code,sr.role_name,sr.role_remake, "+
//			" sr.role_state,sr.create_operator,sr.create_time,sr.update_time  "+
//			" from boss_user_role as ur,boss_shiro_role sr "+
//			" where ur.role_id = sr.id "+
//			" and ur.user_id=#{userId} ")
//	@ResultMap("cn.huobi.framework.dao.UserRoleMapper.OneToOneResultMap")
//	List<UserRole> findUserRoleByUserId(@Param("userId")Integer userId);
//
//}
