package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.SysDictDao;
import cn.huobi.framework.dao.UserDao;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.*;
import cn.huobi.framework.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	@Resource
	private UserDao userDao;
	
	@Resource
	private SysDictDao sysDictDao;
	
	
	@Override
	public void selectUserByCondition(UserInfo user, Page<UserInfo> page) {
		userDao.selectUserByCondition(user,page);
	}
	
	@Override
	public int insertUser(UserInfo user) {
		if(user!=null){
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user.setCreateOperator(principal.getId().toString());
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			user.setPassword(passEnc.encodePassword("123456", user.getUserName()));
//			user.setPassword("123456");
		} else {
			return 0;
		}
		return userDao.saveUser(user);
	}
	
	@Override
	public int updateUser(UserInfo user) {
		return userDao.updateUser(user);
	}
	
	@Override
	public int deleteUser(Integer userId) {
		if(userId==1){
			return 0;
		}
		userDao.delUserRoles(userId);
		userDao.delUserRights(userId);
		int num=userDao.delUser(userId);
		return num;
	}

	@Override
	public Map<String, Object> checkNameUnique(String userName) {
		Map<String, Object> msg = new HashMap<>();
		Integer num = userDao.checkNameUnique(userName);
		if(num != null){
			msg.put("msg", "用户名已存在");
			msg.put("status", false);
		}  else {
			msg.put("msg", "用户名可以使用");
			msg.put("status", true);
		}
		return msg;
	}
	
	
	@Override
	public List<RoleInfo> getRolesByUser(Integer id) {
		return userDao.getRolesByUser(id);
	}
	@Override
	public List<RightInfo> getRightsByUser(Integer id) {
		return userDao.getRightsByUser(id);
	}

	@Override
	public List<UserInfo> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Override
	public int restPwd(Integer id) {
		UserInfo user = userDao.selectInfo(id);
		if(user!=null){
			user.setUpdatePwdTime(new Date());
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			user.setPassword(passEnc.encodePassword(sysDictDao.selectRestPwd().getSysValue(), user.getUserName()));
		} else {
			return 0;
		}
		return userDao.restPwd(user);
	}

	@Override
	public int updateUserPwd(Map<String, Object> param) {
		try {
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			String pwds=passEnc.encodePassword((String) param.get("pwd"), param.get("name"));
			UserInfo user = userDao.selectInfo((Integer)param.get("id"));
			if(user!=null){
				if(!user.getPassword().equals(pwds)){
					return 2;
				}
				user.setUpdatePwdTime(new Date());
				user.setPassword(passEnc.encodePassword((String) param.get("newPwd"),user.getUserName()));
				int i = userDao.restPwd(user);
				if(i>0){
					return 1;
				}else{
					return 0;
				}
			}
		} catch (Exception e) {
			log.error("修改密码失败");
		}
//		userDao.updateUserPwd(user)
		
		return 0;
	}

	@Override
	public List<MenuInfo> getMenuByUser(Integer id) {
		return userDao.getMenuByUser(id);
	}

	//商户缠重置密码
	public int merchantRestPwd(String telNo, String teamId) {
		int num=0;
		try {
			UserInfo user = userDao.selectInfoByTelNo(telNo,teamId);
			if(user!=null){
				user.setUpdatePwdTime(new Date());
				user.setTeamId(teamId);
				user.setTelNo(telNo);
				Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
				user.setPassword(passEnc.encodePassword(sysDictDao.selectRestPwd().getSysValue(), user.getTelNo()));
			} else {
				return 0;
			}
			num = userDao.merchantRestPwd(user);
		} catch (Exception e) {
			System.out.println(e);
		}
		return num;
	}
//	@Override
//	public List<RoleInfo> getOtherRolesByUser(Integer id) {
//		return userDao.getOtherRolesByUser(id);
//	}

	@Override
	public List<UserInfo> selectUserByMenuCode(String menuCode) {
		return userDao.selectUserByMenuCode(menuCode);
	}

	@Override
	public List<UserInfo> findUserBox() {
		return userDao.findUserBox();
	}

	@Override
	public UserInfo selectUserByUserName(String userName) {
		return userDao.selectUserByUserName(userName);
	}

	@Override
	public UserInfo selectInfoByTelNo(String telNo, String teamId) {
		return userDao.selectInfoByTelNo(telNo, teamId);
	}

	@Override
	public int updateInfoByMp(String telNo, String newTelNo, String teamId) {
		Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
		String pwd=passEnc.encodePassword(sysDictDao.selectRestPwd().getSysValue(), newTelNo);
		return userDao.updateInfoByMp(telNo, newTelNo, teamId, pwd);
	}

	@Override
	public List<UserInfo> getUserlimit(UserInfo user) {
		return userDao.getUserlimit(user);
	}

	@Override
	public UserInfo getUserInfoById(Integer id) {
		return userDao.getUserInfoById(id);
	}

}
