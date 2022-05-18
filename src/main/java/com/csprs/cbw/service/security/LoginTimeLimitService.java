package com.csprs.cbw.service.security;


import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;


@Service
public class LoginTimeLimitService {
	
	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	
	// 登入封鎖期限
	private static final int UNLOCK_TIME = 1;

	/** @loginCountLimitProccess()
	 *  從session中獲取並計算登入失敗的次數，如果超過3次以上，
	 *  則記下封鎖時間後幾分鐘的session並返回true說明開始封鎖
	 * */
	public boolean loginCountLimitProccess(ServletWebRequest servletWebRequest, SessionStrategy sessionStrategy) {
    	log.info("[SOUT] >> 登入錯誤");
    	if(sessionStrategy.getAttribute(servletWebRequest, "errorCnt") == null) {
    		log.info("[SOUT] >> 登入錯誤計算開始");
    		sessionStrategy.setAttribute(servletWebRequest, "errorCnt", 1);
    	}else {
			int errorCnt = (int)sessionStrategy.getAttribute(servletWebRequest, "errorCnt") + 1;
			sessionStrategy.setAttribute(servletWebRequest, "errorCnt", errorCnt);
			if(errorCnt >= 3) {
				sessionStrategy.setAttribute(servletWebRequest, "errorCnt", 0);
				// 還需要設定錯誤的時間，只要驗證太多次就先鎖1分鐘
				sessionStrategy.setAttribute(servletWebRequest, "errorTime", unLockTime());
				log.info("[SOUT] >> 登入進行封鎖");
				return true;
			}
		}
    	return false;
    }
	
	/** @unLockTime()
	 *  設定當前時間加上多少分鐘作為登入封鎖的期限
	 *  則記下封鎖時間後幾分鐘的session並返回true說明開始封鎖
	 * */
    private Date unLockTime() {
    	Calendar currentTimeNow = Calendar.getInstance();
    	log.info("[SOUT] >> 當前時間 : " + currentTimeNow.getTime());
    	currentTimeNow.add(Calendar.MINUTE, UNLOCK_TIME);
    	Date tenMinsFromNow = currentTimeNow.getTime(); 
        
        return tenMinsFromNow;
    }
    
    /** @checkUnLockTime()
	 *  在進入系統時會先查看是否有登入封鎖時間的session
	 *  如果還在封鎖，就返回true
	 *  如果已經過了封鎖時間，則清除session並返回false
	 * */
    public boolean checkUnLockTime(ServletWebRequest servletWebRequest, SessionStrategy sessionStrategy){
    	if(sessionStrategy.getAttribute(servletWebRequest, "errorTime") != null) {
    		Date date = (Date)sessionStrategy.getAttribute(servletWebRequest, "errorTime");
            Calendar currentTimeNow = Calendar.getInstance();
            Date now = currentTimeNow.getTime();
            if (now.before(date)) {
            	log.info("[SOUT] >> 登入頁面封鎖中 ...");
            	return true; // 還在鎖定時間
    		}else {
    			log.info("[SOUT] >> 登入頁面封鎖解除，清除session");
    			if(sessionStrategy.getAttribute(servletWebRequest, "errorTime") != null) {
    				sessionStrategy.removeAttribute(servletWebRequest, "errorTime");
    			}
    			return false; // 鎖定時間過了
			}
    	}
    	return false;
    }
    
   
	
}
