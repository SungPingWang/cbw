package com.csprs.cbw.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.csprs.cbw.util.Constant;


@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	
	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	@Autowired
	private TimeLimitService loginTimeLimitService;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {
		//String loginuser = (String)request.getParameter("username");
		//logger.error("登入失敗: " + loginuser);
		//response.sendRedirect("/csprscbw/login?error");
		log.error("登入錯誤");
        // 返回是否登入超過次數
        boolean isLimit = loginTimeLimitService.CountLimitProccess(
        		new ServletWebRequest(httpServletRequest), sessionStrategy, 
        		Constant.SESSION_LOGIN_ERROR_COUNT_NAME, 
        		Constant.SESSION_MAX_LOGIN_ERROR_COUNT,
        		Constant.SESSION_LOGIN_ERROR_DUETIME_NAME);
    	/*httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.setContentType("application/json;charset=utf-8");*/
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.setContentType("text/html;charset=utf-8");
        // 把先前 throw new ValidateCodeException("驗證碼 ERROR"); 錯誤丟入輸出	 
        if (isLimit) {
			httpServletResponse.getWriter().print("<p>驗證次數過多,你已被鎖定登入1分鐘，請稍後再試...</p>");
		}else if("TimeLimitError".equals(e.getMessage())) {
			httpServletResponse.getWriter().print("<p>你已被鎖定登入1分鐘，提醒您不斷嘗試刷新頁面將重新計算鎖定時間...</p>");
		}else {
			//httpServletResponse.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				log.error("[ERRO] >> 睡眠出現錯誤");
			}
			httpServletResponse.sendRedirect("/csprscbw/error/login_failed/failed");
		}
	}
	
}
