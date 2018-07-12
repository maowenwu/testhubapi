package cn.huobi.boss.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	private final Log logger = LogFactory.getLog(getClass());
	public AjaxLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.debug("commence: "+authException.getLocalizedMessage());
		HttpServletRequest httpRequest = (HttpServletRequest)request;
        if ("XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"SessionTimeout");
        } else{
            super.commence(request, response, authException);
        }
	}

}
