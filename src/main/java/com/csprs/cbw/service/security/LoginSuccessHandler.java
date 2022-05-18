package com.csprs.cbw.service.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		String name = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // [ROLE_ADMIN, ROLE_USER]
		System.out.println("你已經進入了權限: " + authorities);
		HttpSession session = request.getSession();
		session.setAttribute("UserRole", name);
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
