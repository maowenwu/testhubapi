package cn.huobi.framework.service;

import java.util.List;

import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;

public interface UserGrantService {
	//用户授权角色
	int updateRoleToUser(Integer userId, List<Integer> roleList);
	//用户授权权限
	int updateRightToUser(Integer userId, List<Integer> rightList);
	//用户添加菜单
	int updateMenuToUser(Integer userId, List<Integer> menuIds);

	//角色授权用户
	int updateRoleAddUser(Integer roleId, List<Integer> userList);
	//角色添加权限
	int updateRightToRole(Integer roleId, List<Integer> rigthList);

	//权限授权用户
	int updateRightAddUser(Integer rightId, List<Integer> userList);
	//权限添加角色
	int updateRightAddRole(Integer rightId, List<Integer> roleList);

	//权限添加菜单
	int updateMenuToRight(Integer right, List<Integer> menuList);
	//菜单添加到权限上
	int updateRightToMenu(Integer menu, List<Integer> rightList);

	//删除用户角色
	int deleteUserRole(Integer userId, Integer roleId);
	int insertUserRole(Integer userId, Integer roleId);
	//删除用户权限
	int deleteUserRight(Integer userId, Integer rightId);
	int insertUserRight(Integer userId, Integer rightId);
	//删除角色权限
	int deleteRoleRight(Integer roleId, Integer rightId);
	int insertRoleRight(Integer roleId, Integer rightId);
	//删除权限菜单
	int deleteRightMenu(Integer roleId, Integer rightId);
	int insertRightMenu(Integer roleId, Integer rightId);
	
	//用户所有的权限
	List<RightInfo> getUserAllRight(Integer userId);
	//用户所有的菜单
	List<MenuInfo> getUserAllMenu(Integer uId);
	
}
