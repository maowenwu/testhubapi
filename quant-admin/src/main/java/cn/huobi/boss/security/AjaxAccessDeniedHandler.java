package cn.huobi.boss.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfException;

public class AjaxAccessDeniedHandler implements AccessDeniedHandler {
	private final Log logger = LogFactory.getLog(getClass());
	AccessDeniedHandler defaultHandler = new AccessDeniedHandlerImpl();
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if(logger.isDebugEnabled()){
			logger.debug(accessDeniedException.getLocalizedMessage());
		}
		if(accessDeniedException instanceof CsrfException){
			// 可能是session丢失导致csrf失败
			handleCheckRequest(request, response, accessDeniedException);
		}else if(accessDeniedException instanceof AuthorizationServiceException){
			
		}
		defaultHandler.handle(request, response, accessDeniedException);
	}

	private void handleCheckRequest(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if(checkRequest(request)){
			if(logger.isDebugEnabled()){
				logger.debug("checkRequest returns true, return 449");
			}
			response.sendError(449, "login");
		}else{
			defaultHandler.handle(request, response, accessDeniedException);
		}
	}

	private boolean checkRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}

}
