package com.csprs.cbw.service.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import com.csprs.cbw.util.Constant;

public interface ITimeLimitService {
	
	public boolean CountLimitProccess(ServletWebRequest servletWebRequest, 
			SessionStrategy sessionStrategy, String sessionErrorCountName,
			int sessionErrorMaxCount, String sessionErrorDueTime);
	
	/** @unLockTime()
	 *  設定當前時間加上多少分鐘作為登入封鎖的期限
	 *  則記下封鎖時間後幾分鐘的session並返回true說明開始封鎖
	 * */
	public static Date unLockTime() {
    	Calendar currentTimeNow = Calendar.getInstance();
    	currentTimeNow.add(Calendar.MINUTE, Constant.UNLOCK_TIME_MIN);
    	Date tenMinsFromNow = currentTimeNow.getTime(); 
        
        return tenMinsFromNow;
    }
	
	public boolean checkUnLockTime(ServletWebRequest servletWebRequest, 
			SessionStrategy sessionStrategy,
			String sessionErrorDueTime);
	
}
