package com.csprs.cbw.service.security;


import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;


@Service
public class TimeLimitService implements ITimeLimitService {
	
	private final Logger log = LoggerFactory.getLogger("CustomOperateLog");
	
	/** @loginCountLimitProccess()
	 *  從session中獲取並計算登入失敗的次數，如果超過3次以上，
	 *  則記下封鎖時間後幾分鐘的session並返回true說明開始封鎖
	 * */
	@Override
	public boolean CountLimitProccess(ServletWebRequest servletWebRequest, SessionStrategy sessionStrategy,
			String sessionErrorCountName, int sessionErrorMaxCount, String sessionErrorDueTime) {
		log.info("登入錯誤");
    	if(sessionStrategy.getAttribute(servletWebRequest, sessionErrorCountName) == null) {
    		log.info("登入錯誤計算開始");
    		sessionStrategy.setAttribute(servletWebRequest, sessionErrorCountName, 1);
    	}else {
    		System.out.println(sessionStrategy.getAttribute(servletWebRequest, sessionErrorCountName));
			int errorCnt = (int)sessionStrategy.getAttribute(servletWebRequest, sessionErrorCountName) + 1;
			sessionStrategy.setAttribute(servletWebRequest, sessionErrorCountName, errorCnt);
			if(errorCnt >= sessionErrorMaxCount) {
				sessionStrategy.setAttribute(servletWebRequest, sessionErrorCountName, 0);
				// 還需要設定錯誤的時間，只要驗證太多次就先鎖1分鐘
				sessionStrategy.setAttribute(servletWebRequest, sessionErrorDueTime, ITimeLimitService.unLockTime());
				log.info("登入進行封鎖");
				return true;
			}
		}
    	return false;
	}
	
    
    /** @checkUnLockTime()
	 *  在進入系統時會先查看是否有登入封鎖時間的session
	 *  如果還在封鎖，就返回true
	 *  如果已經過了封鎖時間，則清除session並返回false
	 * */
	@Override
    public boolean checkUnLockTime(ServletWebRequest servletWebRequest, SessionStrategy sessionStrategy, String sessionErrorDueTime){
    	if(sessionStrategy.getAttribute(servletWebRequest, sessionErrorDueTime) != null) {
    		Date date = (Date)sessionStrategy.getAttribute(servletWebRequest, sessionErrorDueTime);
            Calendar currentTimeNow = Calendar.getInstance();
            Date now = currentTimeNow.getTime();
            if (now.before(date)) {
            	log.info("登入頁面封鎖中 ...");
            	return true; // 還在鎖定時間
    		}else {
    			log.info("登入頁面封鎖解除，清除session");
    			if(sessionStrategy.getAttribute(servletWebRequest, sessionErrorDueTime) != null) {
    				sessionStrategy.removeAttribute(servletWebRequest, sessionErrorDueTime);
    			}
    			return false; // 鎖定時間過了
			}
    	}
    	return false;
    }

	

    
   
	
}
