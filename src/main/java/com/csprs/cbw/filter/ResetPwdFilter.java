package com.csprs.cbw.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.csprs.cbw.service.security.TimeLimitService;
import com.csprs.cbw.util.Constant;

// 在進行密碼重設的時候理論上是已經登入的了，所以要避免使用者直接壞壞想要直接進入主頁面
@Component
public class ResetPwdFilter extends OncePerRequestFilter {
	
	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private TimeLimitService loginTimeLimitService;
	// 引入 session
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		boolean isTimeLimit = validateTimeLimit(new ServletWebRequest(request), response, sessionStrategy);
		if(isTimeLimit && !Constant.LOCK_PATH.equals(request.getRequestURI())) {
			log.warn("重設密碼次數過多進入鎖定期間");
			response.sendRedirect(Constant.LOCK_PATH);
			return;
		}
		
		filterChain.doFilter(request, response);
	}
	
	/** @validateTimeLimit
     * 查看系統是不是處於登入錯誤次數過多被鎖的狀態
     * */
    private boolean validateTimeLimit(ServletWebRequest servletWebRequest, HttpServletResponse response, SessionStrategy sessionStrategy) 
    		throws IOException {
    	/* 調用checkUnLockTime()方法
    	 * 查看session中的鎖定時間是不是已經過了
    	 * */
    	boolean checkUnLockTime = loginTimeLimitService.checkUnLockTime(servletWebRequest, sessionStrategy, Constant.SESSION_RESET_PWD_ERROR_DUETIME_NAME);
		return checkUnLockTime;
    }

}
