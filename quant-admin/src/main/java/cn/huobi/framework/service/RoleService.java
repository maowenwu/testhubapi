package cn.huobi.framework.service;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;

import java.util.List;

public interface RoleService {
	//查询角色
	Page<RoleInfo> selectRoleByCondition(RoleInfo role, Page<RoleInfo> page);
	//新增角色
	int insertRole(RoleInfo role);
	//修改角色
	int updateRole(RoleInfo role);
	//删除角色
	int deleteRoles(Integer roleId);
	
	//角色已授权的用户
	List<UserInfo> getUsersByRole(Integer id);
	//角色已授权的权限
	List<RightInfo> getRightsByRole(Integer id);
	
	List<RoleInfo> getAllRoles();

}
