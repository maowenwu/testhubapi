package cn.huobi.framework.service.sysUser.impl;
//package cn.huobi.framework.service.sysUser.impl;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.collections.ListUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import cn.huobi.framework.dao.sysUser.UserRigthDao;
//import cn.huobi.framework.model.ShiroRigth;
//import cn.huobi.framework.model.UserRight;
//import cn.huobi.framework.service.sysUser.ShiroRigthService;
//import cn.huobi.framework.service.sysUser.UserRigthService;
//@Service("userRigthService")
//@Transactional
//public class UserRigthServiceImpl implements UserRigthService {
//	@Resource
//	public UserRigthDao userRigthDao;
//	@Resource
//	public ShiroRigthService shiroRigthService;
//
//	@Override
//	public int insertUserRigth(Integer userId, Integer rigthId) throws Exception {
//		return userRigthDao.insertUserRigth(userId, rigthId);
//	}
//	@Override
//	public int deleteUserRigth(Integer userId, Integer rigthId) throws Exception {
//		return userRigthDao.deleteUserRigth(userId, rigthId);
//	}
//	@Override
//	public int saveUserRigth(Integer uId,Integer parentId, String[] rigthCodeArray) throws Exception {
//		int i = 0;
//		List<ShiroRigth> selectCheckBoxs = new ArrayList<>();
//		List<ShiroRigth> unSelectCheckBoxs = new ArrayList<>();//没有勾选的checkbox
//		List<ShiroRigth> shiroRigths = shiroRigthService.findShiroRigthByParentId(parentId);
//		for (ShiroRigth shiroRigth : shiroRigths) {//清空原有的
//			int n = this.deleteUserRigth(uId, shiroRigth.getId());
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
//		unSelectCheckBoxs = (List<ShiroRigth>) ListUtils.subtract(shiroRigths, selectCheckBoxs);
//		List<ShiroRigth> userRigths = shiroRigthService.findUserWithRolesPrivilegeRigthByParentId(uId,parentId);//通过角色授权的用户权限
//		userRigths = new ArrayList(new HashSet(userRigths));//去掉重复的
//		List<ShiroRigth> srs1 =  ListUtils.subtract(selectCheckBoxs,userRigths);//打勾的 减去 角色授权的 (要插入的)
//		List<ShiroRigth> srs2 =  ListUtils.intersection(unSelectCheckBoxs,userRigths);//没有打勾的 和角色授权的交集 (要插入的)
//		List<ShiroRigth> srs = ListUtils.union(srs1, srs2);
//		srs = new ArrayList(new HashSet(srs));//去掉重复的
//		for (ShiroRigth shiroRigth : srs) {
//			int n = this.insertUserRigth(uId, shiroRigth.getId());
//			i += n;
//		}
//		return i;
//	}
//	@Override
//	public List<UserRight> findUserRigthByUserId(Integer userId) throws Exception {
//		return userRigthDao.findUserRigthByUserId(userId);
//	}
//	@Override
//	public int deleteUserRigthByRigthId(Integer rigthId) throws Exception {
//		return userRigthDao.deleteUserRigthByRigthId(rigthId);
//	}
//
//
//
//	
//}
