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
//import cn.huobi.framework.dao.sysUser.RoleRightDao;
//import cn.huobi.framework.model.RoleRigth;
//import cn.huobi.framework.model.ShiroRigth;
//import cn.huobi.framework.service.sysUser.RoleRigthService;
//import cn.huobi.framework.service.sysUser.ShiroRigthService;
//import cn.huobi.framework.service.sysUser.UserRigthService;
////@Service("roleRigthService")
////@Transactional
//public class RoleRigthServiceImpl implements RoleRigthService {
//	@Resource
//	public RoleRightDao roleRigthDao;
//	@Resource
//	public ShiroRigthService shiroRigthService;
//	@Resource
//	public UserRigthService userRigthService;
//	@Override
//	public int insertRoleRigth(Integer roleId, Integer rigthId) throws Exception {
//		return roleRigthDao.insertRoleRigth(roleId, rigthId);
//	}
//	@Override
//	public int deleteRoleRigth(Integer roleId, Integer rigthId) throws Exception {
//		return roleRigthDao.deleteRoleRigth(roleId, rigthId);
//	}
//	@Override
//	public List<RoleRigth> findRoleRigthByRoleId(Integer roleId) throws Exception {
//		return roleRigthDao.findRoleRigthByRoleId(roleId);
//	}
//	@Override
//	public int saveRoleRigth(Integer rId, Integer menuId, String[] rigthCodeArray) throws Exception {
//		List<ShiroRigth> selectCheckBoxs = new ArrayList<>();
//		List<ShiroRigth> shiroRigths = shiroRigthService.findShiroRigthByParentId(menuId);
//		int i = 0;
//		for (ShiroRigth shiroRigth : shiroRigths) {//清空原有的
//			int n = this.deleteRoleRigth(rId, shiroRigth.getId());
//			i += n;
//		}
//		for (ShiroRigth shiroRigth : shiroRigths) {
//			for (int j = 0; j < rigthCodeArray.length; j++) {
//				String _rigthCode = rigthCodeArray[j];
//				if (shiroRigth.getRigthCode().equals(_rigthCode)) {
//					selectCheckBoxs.add(shiroRigth);
//					break;
//				}
//			}
//		}
//		for (ShiroRigth shiroRigth : selectCheckBoxs) {
//			int n = this.insertRoleRigth(rId, shiroRigth.getId());
//			int m = userRigthService.deleteUserRigthByRigthId(shiroRigth.getId());//同时删除权限对应(user_rigth)表的所有用户(userId)对应的权限(rigthId)
//			
//			i += n;
//			i += m;
//		}
//		return i;
//	}
//
//	
//
//
//
//	
//}
