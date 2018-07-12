package cn.huobi.boss.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import cn.huobi.framework.dao.MenuDao;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

/**
 * 访问决策管理器
 *
 * 决策某个资源（页面）是否可被当前用户所属的角色访问
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class AccessDecisionManagerImpl implements AccessDecisionManager {
	
	@Resource
	private MenuDao menuDao;
	
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null) {
            return;
        }
        for (ConfigAttribute ca : configAttributes) {
            String needRole = ca.getAttribute();
            List<String> needRoleList = menuDao.getMenuCodeByCode(needRole);
//            List<? extends GrantedAuthority> list = (List<? extends GrantedAuthority>) authentication.getAuthorities();
//            System.out.println(list);
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRoleList.contains(ga.getAuthority())) {
//            	if(needRole.equals(ga.getAuthority())){
            		  return;
                }
            }
        }
        System.out.println("没有足够的权限去访问:" + object);
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
