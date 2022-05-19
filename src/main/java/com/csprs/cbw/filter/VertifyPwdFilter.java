package com.csprs.cbw.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.csprs.cbw.util.Constant;

// 在進行密碼重設的時候理論上是已經登入的了，所以要避免使用者直接壞壞想要直接進入主頁面
@Component
public class VertifyPwdFilter extends OncePerRequestFilter {
	
	// 引入 session
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String delimiter = "\\.";
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		String url = request.getRequestURI();
		List<String> postfixPassList = Arrays.asList("png", "css", "js");
		boolean isStatic = false;
		if(url.split(delimiter).length > 1) {
			isStatic = postfixPassList.contains(url.split(delimiter)[url.split(delimiter).length-1]);
		}

		
		// 1 判斷是否是在驗證密碼重設階段
		if(url.startsWith(Constant.VERTIFY_PWD_PATH)) {
			// 設定session為1
			sessionStrategy.setAttribute(servletWebRequest, Constant.VERTIFY_SESSION, "1");
		// 2 若不為判斷1，如果去了login以外的頁面都必須要返回
		}else if(
				!url.startsWith(Constant.LOGIN_PATH) && !isStatic 
				&& !url.startsWith(Constant.RESET_PWD_PATH) 
				&& !url.startsWith(Constant.ERROR_PATH)
				&& !url.startsWith(Constant.CAPTCHA_PATH)
				&& !url.startsWith(Constant.LOCK_PATH)){
			//System.out.println("有人在亂喔: " + url);
			Object vertifyState = sessionStrategy.getAttribute(servletWebRequest, Constant.VERTIFY_SESSION);
			if("1".equals((String)vertifyState)) {
				response.sendRedirect(Constant.LOGIN_PATH);
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
