package cn.huobi.framework.service.sysUser.impl;
//package cn.huobi.framework.service.sysUser.impl;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import cn.huobi.framework.dao.sysUser.UserDao;
//import cn.huobi.framework.db.pagination.Page;
//import cn.huobi.framework.db.pagination.Sort;
//import cn.huobi.framework.model.ShiroUser;
//import cn.huobi.framework.service.RedisService;
//import cn.huobi.framework.service.sysUser.UserService;
////@Service("userService")
////@Transactional
//public class UserServiceImpl implements UserService {
//	@Resource
//	public UserDao userDao;
//	@Resource
//	private RedisService redisService;
//	
//	private final int MAX_ATTEMPT = 5;//登录最大错误次数
//	
//	@Override
//	public int insertUser(ShiroUser user){
//		return userDao.insert(user);
//	}
//	@Override
//	public List<ShiroUser> getUsers(ShiroUser user,Sort sort,Page<ShiroUser> page) {
//		return userDao.findUsers(user,sort,page);
//	}
//	@Override
//	public List<ShiroUser> findUsersWithRole() {
//		return userDao.findUsersWithRole();
//	}
//	@Override
//	public ShiroUser findUsersWithRoleByUserName(String userName) {
//		return userDao.findUsersWithRoleByUserName(userName);
//	}
//	@Override
//	public ShiroUser findUserById(Integer id) {
//		return userDao.findUserById(id);
//	}
//	@Override
//	public int updateUser(ShiroUser shiroUser) {
//		return userDao.updateUser(shiroUser);
//	}
//	@Override
//	public int updateUserPwd(Integer id, String password) {
//		return userDao.updateUserPwd(id, password);
//	}
//	@Override
//	public boolean isBlocked(String key) {
//		try {
//			int num = 0;
//			if (redisService.exists(key)) {
//				num = Integer.valueOf(redisService.select(key).toString());
//			}
//            return num >= MAX_ATTEMPT;
//        } catch (Exception e) {
//            return false;
//        }
//	}
//
//}
