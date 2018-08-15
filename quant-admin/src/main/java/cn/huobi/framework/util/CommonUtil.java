package cn.huobi.framework.util;

import cn.huobi.framework.model.Result;
import cn.huobi.framework.model.UserLoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static final Pattern patternYMD = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2}$");
    private static final Pattern patternYMDHMS = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2} (\\d){1,2}:(\\d){1,2}:(\\d){1,2}$");


    /**
     * 获取当前登录的UserLoginInfo
     *
     * @return
     */
    public static UserLoginInfo getLoginUser() {
        UserLoginInfo userInfo = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            userInfo = principal;
        } else {
            throw new BossBaseException("用户未登录");
        }
        return userInfo;
    }


}
