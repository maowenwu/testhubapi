package cn.huobi.framework.service.sysUser.impl;
//package cn.huobi.framework.service.sysUser.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import cn.huobi.framework.dao.sysUser.UserRoleDao;
//import cn.huobi.framework.model.ShiroRigth;
//import cn.huobi.framework.model.ShiroRole;
//import cn.huobi.framework.model.UserRole;
//import cn.huobi.framework.service.sysUser.ShiroRigthService;
//import cn.huobi.framework.service.sysUser.ShiroRoleService;
//import cn.huobi.framework.service.sysUser.UserRoleService;
////@Service("userRoleService")
////@Transactional
//public class UserRoleServiceImpl implements UserRoleService {
//	@Resource
//	public UserRoleDao userRoleDao;
////	@Resource
////	public ShiroRigthService shiroRigthService;
//	@Resource
//	public ShiroRoleService shiroRoleService;
//	@Override
//	public int insertUserRole(Integer userId, Integer roleId) throws Exception {
//		return userRoleDao.insertUserRole(userId, roleId);
//	}
//	@Override
//	public int deleteUserRole(Integer userId, Integer role_id) throws Exception {
//		return userRoleDao.deleteUserRole(userId, role_id);
//	}
//	@Override
//	public List<UserRole> findUserRoleByUserId(Integer userId) throws Exception {
//		return userRoleDao.findUserRoleByUserId(userId);
//	}
//	@Override
//	public int saveUserRole(Integer userId, String[] roleIds) throws Exception {
//		List<ShiroRole> selectCheckBoxs = new ArrayList<>();
//		List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
//		int i = 0;
//		this.deleteUserRoleByUserId(userId);//新增角色之前，先删除用户对应的角色
//		
//		for (ShiroRole shiroRole : shiroRoles) {
//			for (int j = 0; j < roleIds.length; j++) {
//				Integer _roleId = Integer.valueOf(roleIds[j]);
//				if (shiroRole.getId().equals(_roleId)) {
//					selectCheckBoxs.add(shiroRole);
//					break;
//				}
//			}
//		}
//		for (ShiroRole sr : selectCheckBoxs) {
//			i = this.insertUserRole(userId, sr.getId());
//		}
//		
//		return i;
//	}
//	@Override
//	public int deleteUserRoleByUserId(Integer userId) throws Exception {
//		return userRoleDao.deleteUserRoleByUserId(userId);
//	}
//
//	
//
//
//
//	
//}
