package com.csprs.cbw.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.csprs.cbw.bean.system.ImageCode;
import com.csprs.cbw.controller.system.ValidateController;
import com.csprs.cbw.exception.ValidateCodeException;
import com.csprs.cbw.service.security.TimeLimitService;
import com.csprs.cbw.util.Constant;


//@Order(1)
//@WebFilter(urlPatterns = "/login/*", filterName = "validateFilter")
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private TimeLimitService loginTimeLimitService;
	// 引入 session
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) 
    		throws ServletException, IOException {
    	
    	/* @validateTimeLimit() 實現
    	 * 判斷是不是因為登入錯誤太多次被鎖住的帳號
    	 * */
    	if(!"/csprscbw/handleLogin/lock".equals(httpServletRequest.getRequestURI())) {
    		try {
        		validateTimeLimit(new ServletWebRequest(httpServletRequest), httpServletResponse, sessionStrategy);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
    	}
    	
    	/* @validateCode() 實現
    	 * 如果是POST方法，並且在進行登入的時候(/security/login 路徑)
    	 * 則開始在validateCode方法中添加對圖形驗證碼的功能
    	 * */
    	if (httpServletRequest.getRequestURI().startsWith("/csprscbw/login")
                && "post".equalsIgnoreCase(httpServletRequest.getMethod())) {
    		log.info("[SOUT] >> 先跑圖形驗證");
            try {
                validateCode(new ServletWebRequest(httpServletRequest), httpServletResponse);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
    	// 都不是就下移至下個Filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    
    /** @validateCode
     * 圖形驗證碼首先進行驗證階段
     * */
    @SuppressWarnings("all")
    private void validateCode(ServletWebRequest servletWebRequest, HttpServletResponse response) 
    		throws ServletRequestBindingException, ValidateCodeException, IOException {

    	log.info("[SOUT] >> 進行登入驗證碼操作");
    	// 提取先前生成的驗證碼答案
    	ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
    	// 提取頁面寫入的驗證碼參數
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
        // 紀錄錯誤狀態
        String errorCount = "";
        
        if (codeInRequest.isEmpty()) {
        	log.info("[SOUT] >> 登入驗證碼為空");
        	errorCount = errorCount + "1";
        }
        if (codeInSession == null) {
        	log.info("[SOUT] >> 沒有登入驗證碼");
        	errorCount = errorCount + "2";
        }
        if (codeInSession.isExpire()) {
        	// 登入驗證碼過期，並且清空session
        	if(sessionStrategy.getAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE) != null) {
        		sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
        	}
            // 過期我就不給他說是錯的好了
            log.info("[SOUT] >> 驗證碼過期");
            // throw new ValidateCodeException("驗證碼已過期！");
            errorCount = errorCount + "3";
        }
        if (!codeInRequest.equalsIgnoreCase(codeInSession.getCode())) {
        	log.info("[SOUT] >> 登入驗證碼錯誤");
        	errorCount = errorCount + "4";
        }
        if(!"".equals(errorCount)) {
        	/* 跳出ValidateCodeException抱錯
        	 * 將錯誤訊息交由外面的authenticationFailureHandler進行蒐集
        	 * */
        	throw new ValidateCodeException("驗證碼 ERROR");	
        }
        // 清空 session
        sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
    }
    
    /** @validateTimeLimit
     * 查看系統是不是處於登入錯誤次數過多被鎖的狀態
     * */
    private void validateTimeLimit(ServletWebRequest servletWebRequest, HttpServletResponse response, SessionStrategy sessionStrategy) 
    		throws IOException {
    	/* 調用checkUnLockTime()方法
    	 * 查看session中的鎖定時間是不是已經過了
    	 * */
    	boolean checkUnLockTime = loginTimeLimitService.checkUnLockTime(servletWebRequest, sessionStrategy, Constant.SESSION_LOGIN_ERROR_DUETIME_NAME);
		if(checkUnLockTime) { // 還在鎖定時間
			log.info("[SOUT] >> 鎖定期間");
			/* 跳出ValidateCodeException抱錯
        	 * 將錯誤訊息交由外面的authenticationFailureHandler進行蒐集
        	 * */
			throw new ValidateCodeException("TimeLimitError");	
		}
    }
}
