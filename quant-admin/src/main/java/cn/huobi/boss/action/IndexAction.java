package cn.huobi.boss.action;

import cn.huobi.boss.system.DataSource;
import cn.huobi.framework.model.SysDict;
import cn.huobi.framework.model.SysMenu;
import cn.huobi.framework.model.UserLoginInfo;
import cn.huobi.framework.service.MenuService;
import cn.huobi.framework.service.SysDictService;
import cn.huobi.framework.util.Constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class IndexAction {
	@Resource
    MenuService menuService;
	
	@Resource
	SysDictService sysDictService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/welcome.do")
	public String welcome(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		System.out.println("welcome");
		Object principalObj = null;
		String verNo = "2.0.001";
		try {
			principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//获取js的版本号
			SysDict sysDict = sysDictService.getByKey("VER_NUM");
			if(sysDict!=null){
				verNo = sysDict.getSysValue();
			}
			model.addAttribute("verNo", verNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(principalObj instanceof UserLoginInfo){
			final UserLoginInfo principal = (UserLoginInfo) principalObj;
			Set<String> permits = new HashSet<String>();
			for(GrantedAuthority item : principal.getAuthorities()){
				permits.add(item.getAuthority());
			}
			model.addAttribute("permits", permits);
			model.addAttribute("permitsJSON", JSON.toJSONString(permits));
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
			final Set<String> excludes = filter.getExcludes();
			excludes.add("authorities");
			excludes.add("password");
			excludes.add("accountNonExpired");
			excludes.add("accountNonLocked");
			excludes.add("credentialsNonExpired");
			excludes.add("enabled");
			model.addAttribute("principalJSON", JSON.toJSONString(principal,filter));
		}else{
			model.addAttribute("permits", Collections.EMPTY_SET);
			model.addAttribute("permitsJSON", "[]");
			model.addAttribute("principalJSON", "{}");
		}
		return "index";
	}
	// 权限控制相关页面

    @RequestMapping(value = "/login.do")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/denied.do")
    public String deniedPage() {
        return "denied";
    }

	@DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping({"/views/common/navigation.html","/navigation.do"})
    public String navigation(ModelMap model){
    	try {
    		Object principalObj = null;
    		try {
    			principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		if(principalObj instanceof UserLoginInfo){
				final UserLoginInfo principal = (UserLoginInfo) principalObj;
	    		Set<String> permits = new HashSet<String>();
	    		for(GrantedAuthority item : principal.getAuthorities()){
	    			permits.add(item.getAuthority());
	    		}
//	    		System.out.println(permits);
				List<SysMenu> list = menuService.getSysMenuAndChildren();
//				List<Integer> menuIds = menuService.getMenuIdsByPermits(permits);
				
				filterMenu(list, permits);
				model.addAttribute("menus", list);
    		}else{
    			model.addAttribute("menus", null);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "navigation";
    }
    
	private void filterMenu(Collection<SysMenu> list, Collection<String> menuIds) {
		Iterator<SysMenu> it = list.iterator();
		while(it.hasNext()){
			SysMenu menu = it.next();
//			if(!menuIds.contains(menu.getId().toString())){
			if(!menuIds.contains(menu.getMenuCode().toString())){
				it.remove();
				continue;
			}
			final List<SysMenu> children = menu.getChildren();
			if(children !=null && !children.isEmpty())
				filterMenu(children, menuIds);
		}
	}
}
