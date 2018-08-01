package cn.huobi.boss.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.huobi.framework.model.MenuInfo;
import cn.huobi.framework.model.UserInfo;
import cn.huobi.framework.model.UserLoginInfo;
import cn.huobi.framework.service.MenuService;
import cn.huobi.framework.service.UserGrantService;
import cn.huobi.framework.service.UserService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * 用户详细信息类
 * <p>
 * 负责以{@link UserDetails}方式提供用户信息
 * <p>
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    public UserService userService;
    @Resource
    public UserGrantService userGrantService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserLoginInfo userInfo = null;
        UserInfo shiroUser = userService.selectUserByUserName(userName);
        try {
            if (shiroUser != null) {
                Integer uId = shiroUser.getId();
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                List<MenuInfo> userRigths = userGrantService.getUserAllMenu(uId);//用户拥有的所有菜单ID
                for (MenuInfo sr : userRigths) {
                    if (sr != null)
                        authorities.add(new SimpleGrantedAuthority(sr.getMenuCode()));
                }

                userInfo = new UserLoginInfo(shiroUser.getUserName(), shiroUser.getPassword(), authorities);
                userInfo.setId(shiroUser.getId());
                userInfo.setRealName(shiroUser.getRealName());
                userInfo.setTelNo(shiroUser.getTelNo());
                userInfo.setEmail(shiroUser.getEmail());
                userInfo.setStatus(shiroUser.getStatus());
                userInfo.setTheme(shiroUser.getTheme());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
