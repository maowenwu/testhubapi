package cn.huobi.framework.service;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.RightInfo;
import cn.huobi.framework.model.RoleInfo;
import cn.huobi.framework.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {
	// 查询用户
	void selectUserByCondition(UserInfo user, Page<UserInfo> page);

	// 新增用户
	int insertUser(UserInfo user);

	// 修改用户
	int updateUser(UserInfo user);
	
	//重置密码
	int restPwd(Integer id);

	//修改密码
	int updateUserPwd(Map<String, Object> param);

	// 删除用户
	int deleteUser(Integer userId);

	Map<String, Object> checkNameUnique(String userName);

	// 用户已授权的角色
	List<RoleInfo> getRolesByUser(Integer id);

	// 用户已授权的权限
	List<RightInfo> getRightsByUser(Integer id);

	List<UserInfo> getAllUsers();

	List<MenuInfo> getMenuByUser(Integer id);

	List<UserInfo> selectUserByMenuCode(String menuCode);

	List<UserInfo> findUserBox();

	UserInfo selectUserByUserName(String userName);

	UserInfo selectInfoByTelNo(String telNo, String teamId);

	int updateInfoByMp(String telNo, String newTelNo, String teamId);

	List<UserInfo> getUserlimit(UserInfo user);

	UserInfo getUserInfoById(Integer id);
}
