package com.csprs.cbw.handler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.csprs.cbw.bean.user.LoginInfo;
import com.csprs.cbw.service.manage.LatestNewService;
import com.csprs.cbw.service.user.LoginInfoService;
import com.csprs.cbw.util.Constant;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private LatestNewService latestNewService;
	@Autowired
	private LoginInfoService loginInfoService;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		String name = authentication.getName();
		//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // [ROLE_ADMIN, ROLE_USER]
		HttpSession session = request.getSession();
		session.setAttribute("UserRole", name);
		// 這裡可以針對一些警告進行session的設定，像是密碼不符合資安
		List<String> checkWarningMessage = latestNewService.checkWarningMessage(name);
		session.setAttribute("WarningMsgs", checkWarningMessage);
		//response.sendRedirect(request.getContextPath() + "/cwb/index");
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setAccount(name);
		loginInfo.setLoginTime(new Date());
		loginInfoService.save(loginInfo);
		response.sendRedirect(Constant.HOME_PATH);
	}

	
	
}
