package com.csprs.cbw.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.csprs.cbw.util.Constant;

// 在進行密碼重設的時候理論上是已經登入的了，所以要避免使用者直接壞壞想要直接進入主頁面
@Component
public class HomeFilter extends OncePerRequestFilter {
	
	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	  
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(Constant.BASE_PATH.equals(request.getRequestURI())) {
			response.sendRedirect(Constant.HOME_PATH);
		}
		filterChain.doFilter(request, response);
	}
	
}
