package com.csprs.cbw.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.csprs.cbw.service.manage.LatestNewService;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private LatestNewService latestNewService;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		System.out.println("成功");
		String name = authentication.getName();
		//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // [ROLE_ADMIN, ROLE_USER]
		HttpSession session = request.getSession();
		session.setAttribute("UserRole", name);
		// 這裡可以針對一些警告進行session的設定，像是密碼不符合資安
		String checkWarningMessage = latestNewService.checkWarningMessage(name);
		if(!"".equals(checkWarningMessage)) {
			session.setAttribute("WarningMsgs", checkWarningMessage);
		}
		response.sendRedirect(request.getContextPath());
	}

	
	
}
