package cn.huobi.boss.action;

import cn.huobi.boss.system.SystemLog;
import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.*;
import cn.huobi.framework.service.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.entity.BossShiroUser;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统用户
 * @author 沙
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserAction {
	
	private static final Logger log = LoggerFactory.getLogger(UserAction.class);
	
	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private RightService rightService;
	
	@Resource
	private MenuService menuService;
	
	@Resource
	private UserGrantService userGrantService;

	/**
	 * 系统用户条件分页查询
	 * @param baseInfo
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/selectUserByCondition.do")
	@ResponseBody
	public PageInfo<BossShiroUser> selectUserByCondition(@RequestParam("baseInfo") String baseInfo,
														 @Param("page") PageInfo<BossShiroUser> page) {
		BossShiroUser bossShiroUser = JSON.parseObject(baseInfo, BossShiroUser.class);
		page=userService.selectPage(bossShiroUser, page);
		return page;
	}
	
	//新增系统用户
	@RequestMapping(value="/saveUser.do")
	@ResponseBody
	@SystemLog(description = "新增系统用户",operCode="user.insert")
	public Map<String, Object> saveUser(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			UserInfo user = JSONObject.parseObject(json.getString("user"), UserInfo.class);
			int num = userService.insertUser(user);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "添加成功！");
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "添加失败！");
			log.error("新增用户失败");
		}
		return msg;
	}
	//修改用户
	@RequestMapping(value="/updateUser.do")
	@ResponseBody
	@SystemLog(description = "用户修改",operCode="user.update")
	public Map<String, Object> updateUser(@RequestBody String param)  {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSONObject.parseObject(param);
			UserInfo user = JSONObject.parseObject(json.getString("user"), UserInfo.class);
			int num = userService.updateUser(user);
			if(num == 1){
				msg.put("status", true);
				msg.put("msg", "修改成功！");
			}
		} catch (Exception e) {
			msg.put("status", true);
			msg.put("msg", "修改失败！");
			log.error("修改用户失败");
		}
		return msg;
	}
	/**
	 * 验证用户名是否唯一
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkNameUnique.do")
	@ResponseBody
	public Map<String, Object> checkNameUnique(@RequestParam("userName")String userName) {
		Map<String, Object> msg = null;
		try {
			msg = userService.checkNameUnique(userName);
		} catch (Exception e) {
			log.error("验证用户名是否唯一，出现错误");
		}
		return msg;
	}

	@RequestMapping(value="/getRoles.do")
	@ResponseBody
	public Map<String, Object> getRoles(@RequestParam("id")Integer id) {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RoleInfo> userRoles = userService.getRolesByUser(id);
			List<RoleInfo> roleGroup = roleService.getAllRoles();
			msg.put("userRoles", userRoles);
			msg.put("roleGroup", roleGroup);
		} catch (Exception e) {
			log.error("查询相关角色信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserRole.do")
	@ResponseBody
	public Map<String, Object> addUserRole(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> roleIds=JSONArray.parseArray(json.getString("roleIds"),Integer.class);
			int num = userGrantService.updateRoleToUser(userId, roleIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户角色失败");
		}
		return msg;
	}

	@RequestMapping(value="/getRights.do")
	@ResponseBody
	public Map<String, Object> getRights(@RequestParam("id")Integer id){
		Map<String, Object> msg = new HashMap<>();
		try {
			List<RightInfo> userRights = userService.getRightsByUser(id);
			List<RightInfo> rightGroup = rightService.getAllRights();
			msg.put("userRights", userRights);
			msg.put("rightGroup", rightGroup);
		} catch (Exception e) {
			log.error("查询相关权限信息失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserRight.do")
	@ResponseBody
	@SystemLog(description = "添加用户权限",operCode="user.role")
	public Map<String, Object> addUserRight(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> rightIds=JSONArray.parseArray(json.getString("rightIds"),Integer.class);
			int num = userGrantService.updateRightToUser(userId, rightIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户权限失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/deleteUser.do")
	@ResponseBody
	@SystemLog(description = "删除用户",operCode="user.delete")
	public Map<String, Object> deleteUser(@RequestParam("id")Integer id){
		Map<String, Object> msg = new HashMap<>();
		try {
			int num = userService.deleteUser(id);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "删除成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "删除失败");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "删除失败");
			log.error("删除用户失败");
		}
		return msg;
	}
	
	/**
	 * 重置密码
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/restPwd.do")
	@ResponseBody
	public Map<String, Object> restPwd(@RequestParam("id")Integer id){
		Map<String, Object> msg = new HashMap<>();
		try {
			int i =userService.restPwd(id);
			if(i>0){
				msg.put("msg", "重置密码成功:abc888888");
			}else{
				msg.put("msg", "重置密码失败");
			}
		} catch (Exception e) {
			log.error("重置密码失败");
			msg.put("msg", "重置密码失败");
		}
		return msg;
	}

	
	/**
	 * 修改密码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updatePwd.do")
	@ResponseBody
	public Map<String, Object> updatePwd(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			JSONObject json=JSONObject.parseObject(param);
			String pwd=json.getString("pwd");
			String newPwd=json.getString("newPwd");
			Map<String, Object> maps=new HashMap<String, Object>();
			maps.put("id", principal.getId());
			System.out.println(principal.getUsername());
			maps.put("name", principal.getUsername());
			maps.put("pwd", pwd);
			maps.put("newPwd", newPwd);
			System.out.println("pwd=="+pwd+"\t newPWD=="+newPwd);
			int i = userService.updateUserPwd(maps);
			if(i==1){
				msg.put("msg", "修改密码成功");
				msg.put("bols", true);
			}else if(i==2){
				msg.put("msg", "原密码不正确");
				msg.put("bols", false);
			}else{
				msg.put("msg", "修改密码失败");
				msg.put("bols", false);
			}
		} catch (Exception e) {
			log.error("修改密码失败");
			msg.put("msg", "修改密码失败");
		}
		return msg;
	}
	
	/**
	 * 根据用户获取菜单
	 * @param baseInfo
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectMenuByUser.do")
	@ResponseBody
	public Map<String, Object> selectMenuByUser(@RequestParam("baseInfo") String baseInfo,
                                                @Param("page")Page<SysMenu> page){
		Map<String, Object> msg = new HashMap<>();
		SysMenu sysMenu = JSON.parseObject(baseInfo, SysMenu.class);
		try {
			menuService.selectMenuByCondition(page, sysMenu);
			List<MenuInfo> userMenus = userService.getMenuByUser(sysMenu.getId());//这个id其实是用户的id，只是存在这个对象里
			msg.put("page", page);
			msg.put("rightMenus", userMenus);
		} catch (Exception e) {
			log.error("根据权限查询菜单，条件查询失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/editUserMenu.do")
	@ResponseBody
	public Map<String, Object> editUserMenu(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSONObject.parseObject(param);
			Integer userId=JSONObject.parseObject(json.getString("userId"), Integer.class);
			List<Integer> menuIds=JSONArray.parseArray(json.getString("menuIds"),Integer.class);
			int num = userGrantService.updateMenuToUser(userId, menuIds);
			if(num > 0){
				msg.put("status", true);
				msg.put("msg", "添加成功");
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "添加失败");
			log.error("添加用户菜单失败");
		}
		return msg;
	}
	
	/**
	 * 查询拥有某一权限的所有用户
	 * @return
	 */
	@RequestMapping(value="/selectUserByMenuCode.do")
	@ResponseBody
	public List<UserInfo> selectUserByMenuCode(String menuCode){
		List<UserInfo> list = null;
		 try {
			list = userService.selectUserByMenuCode(menuCode);
		} catch (Exception e) {
			log.error("查询拥有某一权限的所有用户失败");
		}
		 return list;
	}

	@RequestMapping(value = "/findUserBox.do")
	@ResponseBody
	public Object findUserBox() {
		List<UserInfo> list=null;
		try {
			list = userService.findUserBox();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//查询用户前50条
	@RequestMapping(value="/getUserlimit")
	@ResponseBody
	public Map<String, Object> getUserlimit(@RequestParam("info")String info) {
		Map<String, Object> map=new HashMap<String, Object>();
		UserInfo user = JSON.parseObject(info, UserInfo.class);
		try {
			List<UserInfo> list=userService.getUserlimit(user);
			map.put("userList",list);
			map.put("status",true);
		} catch (Exception e) {
			log.error("查询用户前100条失败!");
			map.put("msg","查询用户前100条失败!");
			map.put("status",true);
		}
		return map;
	}
}
