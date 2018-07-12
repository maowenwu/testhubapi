package cn.huobi.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;

public interface RoleDao {

	@SelectProvider(type=SqlProvide.class, method="selectRoleByCondition")
	@ResultType(RoleInfo.class)
	List<RoleInfo> selectRoleByCondition(@Param("role")RoleInfo role, Page<RoleInfo> page);

	@Insert("insert into boss_shiro_role (role_code,role_name,role_remark,role_status) values (#{role.roleCode},#{role.roleName},"
			+ "#{role.roleRemark},#{role.roleStatus})")
	int saveRole(@Param("role")RoleInfo role);

	@Update("update boss_shiro_role set role_code=#{role.roleCode},role_name=#{role.roleName},role_remark=#{role.roleRemark},"
			+ "role_status=#{role.roleStatus}  where id=#{role.id}")
	int updateRole(@Param("role")RoleInfo role);

	@Delete("DELETE FROM boss_user_role WHERE role_id=#{roleId}")
	void delUserRoles(Integer roleId);

	@Delete("DELETE FROM boss_role_right WHERE role_id=#{roleId}")
	void delRoleRights(Integer roleId);
	
	@Delete("DELETE FROM boss_shiro_role WHERE id=#{roleId}")
	int delRole(Integer roleId);
	
	@Select("SELECT u.* FROM boss_user_role ur,boss_shiro_user u WHERE u.id=ur.user_id AND ur.role_id=#{id}")
	@ResultType(UserInfo.class)
	List<UserInfo> getUsersByRole(Integer id);
	
	@Select("SELECT r.* FROM boss_role_right rr,boss_shiro_right r WHERE r.id=rr.right_id AND rr.role_id=#{id}")
	@ResultType(RightInfo.class)
	List<RightInfo> getRightsByRole(Integer id);
	
	@Select("select * from boss_shiro_role")
	@ResultType(RoleInfo.class)
	List<RoleInfo> getAllRoles();
	public class SqlProvide{
		
		public String selectRoleByCondition(Map<String, Object> map){
			final RoleInfo role = (RoleInfo)map.get("role");
			return new SQL(){{
					SELECT("*");
					FROM("boss_shiro_role");
					if(role!= null){
						if(StringUtils.isNotBlank(role.getRoleName())){
							role.setRoleName(role.getRoleName()+"%");
							WHERE("role_name like #{role.roleName}");
						}
						//状态不为空，且不为“全部”
						if(role.getRoleStatus()!=null && role.getRoleStatus()!=2){
							WHERE("role_status = #{role.roleStatus}");
						}
					}
				}
			}.toString();
			}
		}

}
