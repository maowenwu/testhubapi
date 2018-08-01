package cn.huobi.boss.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.huobi.framework.db.pagination.Page;
import cn.huobi.framework.model.SysMenu;
import cn.huobi.framework.model.UserLoginInfo;
import cn.huobi.framework.service.MenuService;
import cn.huobi.framework.util.AntUrlPathMatcher;
import cn.huobi.framework.util.CommonUtil;
import cn.huobi.framework.util.UrlMatcher;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 资源元数据类
 * <p>
 * 负责提供资源数据信息
 * <p>
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Logger logger = LoggerFactory.getLogger(SecurityMetadataSource.class);

    private UrlMatcher urlMatcher = new AntUrlPathMatcher();
    @Resource
    public MenuService sysMenuService;

    private static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

    public SecurityMetadataSource() {

    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        try {
            List<SysMenu> sysMenus = sysMenuService.selectMenuByCondition(new Page<SysMenu>(1, Integer.MAX_VALUE), null);
            for (final SysMenu sysMenu : sysMenus) {
                String url = sysMenu.getMenuUrl();
                if (StringUtils.isNotBlank(url) && !url.startsWith("#")) {
                    url = "/" + url + "**";
                } else {
                    continue;
                }
                final ArrayList<ConfigAttribute> value = new ArrayList<ConfigAttribute>();
                value.add(new SecurityConfig(sysMenu.getMenuCode()));
                resourceMap.put(url, value);
            }

            String url = ((FilterInvocation) object).getRequestUrl();
            if (url.contains("export")) {
                UserLoginInfo loginUser = CommonUtil.getLoginUser();
                logger.info("当前用户导出报表{}", url);
                logger.info("当前用户{},{}", loginUser.getId(), loginUser.getRealName());
            }
            for (String resURL : resourceMap.keySet()) {
                if (resURL != null && urlMatcher.pathMatchesUrl(resURL, url)) {
                    return resourceMap.get(resURL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
