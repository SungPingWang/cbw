package com.csprs.cbw.util;

public class Constant {

	public static final int UNLOCK_TIME_MIN = 1; // 登入封鎖期限(分鐘)
	/* 登入錯誤 */
	public final static String SESSION_LOGIN_ERROR_COUNT_NAME = "errorCnt"; // session名稱:登入錯誤計算次數
	public final static String SESSION_LOGIN_ERROR_DUETIME_NAME = "errorTime"; // session名稱:登入錯誤後所定期限
	public final static int SESSION_MAX_LOGIN_ERROR_COUNT = 3; // 登入最大錯誤次數
	
	/* 密碼重設錯誤 */
	public final static String SESSION_RESET_PWD_ERROR_COUNT_NAME = "errorResetPwdCnt"; // session名稱:登入錯誤計算次數
	public final static String SESSION_RESET_PWD_ERROR_DUETIME_NAME = "errorResetPwdTime"; // session名稱:登入錯誤後所定期限
	public final static int SESSION_MAX_RESET_PWD_ERROR_COUNT = 3; // 登入最大錯誤次數
	
	
}
