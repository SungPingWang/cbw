package com.csprs.cbw.util;

public class Constant {
	
	public static final String ONE = "1";
	public static final String IS_OK = "ok";
	
	public static final String SEMICOLON = ";";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String BLANK = "";
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_MANAGER = "ROLE_MANAGER";
	public static final String ROLE_USER = "ROLE_USER";
	
	public static final String AUTH_ADMIN = "ADMIN";
	public static final String AUTH_MANAGER = "MANAGER";
	public static final String AUTH_USER = "USER";
	public static final String AUTH_ADMIN_DESCRIPTION = "superuser";
	public static final String AUTH_MANAGER_DESCRIPTION = "manager";
	public static final String AUTH_USER_DESCRIPTION = "user";

	public static final int UNLOCK_TIME_MIN = 1; // 登入封鎖期限(分鐘)

	/* 登入錯誤 */
	public final static String SESSION_LOGIN_ERROR_COUNT_NAME = "errorCnt"; // session名稱:登入錯誤計算次數
	public final static String SESSION_LOGIN_ERROR_DUETIME_NAME = "errorTime"; // session名稱:登入錯誤後所定期限
	public final static int SESSION_MAX_LOGIN_ERROR_COUNT = 3; // 登入最大錯誤次數
	
	/* 密碼重設錯誤 */
	public final static String SESSION_RESET_PWD_ERROR_COUNT_NAME = "errorResetPwdCnt"; // session名稱:登入錯誤計算次數
	public final static String SESSION_RESET_PWD_ERROR_DUETIME_NAME = "errorResetPwdTime"; // session名稱:登入錯誤後所定期限
	public final static int SESSION_MAX_RESET_PWD_ERROR_COUNT = 3; // 登入最大錯誤次數
	public final static int RESET_PWD_MAILSENDER_EXPIRED = 1; // 登入封鎖期限(分鐘)
	
	/* 可能會用到的相關路徑 */
	public final static String HOME_PATH = "/csprscbw/cwb/index";
	public final static String LOGIN_PATH = "/csprscbw/login";
	public final static String CAPTCHA_PATH = "/csprscbw/code/image";
	public final static String ERROR_PATH = "/csprscbw/error";
	public final static String VERTIFY_PWD_PATH = "/csprscbw/handleLogin/vertify";
	public final static String RESET_PWD_PATH = "/csprscbw/handleLogin/resetPwd";
	public final static String LOCK_PATH = "/csprscbw/handleLogin/lock";
	public final static String VERTIFY_SESSION = "vertifyState";
	
	public final static String[] PWD_ALLOW_SYMBOL = {"!", "@", "#", "$", "%", "&", "*"};
	public final static String[] PWD_NOTALLOW_SYMBOL = {",", ";"};
	
	/* 警告語 */
	public final static String PWD_NOTALLOW_MSG = "您的密碼設定不符合資安規定，請至帳號管理頁面對密碼進行近一步規範的設定。";
	public final static String MAIL_NOTEXIST_MSG = "您的個人資料尚未填入信箱，請進行更新（測試）";
	public final static String PWD_EXPIRED_MSG = "您的密碼已多個月並未進行更改，請更改密碼確保安全（測試）";
	/* error提示語 */
	public final static String LOGIN_FAILED_MSG = "登入驗證錯誤，請重新進行登入。";
	/* cbw_station提示語 */
	public final static String PAGINATION_NOT_FOUND = "相關頁面不存在或出現錯誤 !!!";
	/* 帳號增改警告語 */
	public final static String ACCOUNT_EXISTED = "此帳號已存在 !!!";
	public final static String ACCOUNT_NOAUTH_CRUD = "您的權限無法對他人進行增刪改查操作 !!!";
	public final static String PWD_CRUD_NOTALLOW_MSG = "密碼不符合資安認證 !!!";
	public final static String PWD_CRUD_DUPLICATE_MSG = "與前三筆中的其中一筆密碼相同 !!!";
	/* 忘記密碼重設系列輸出提示語 */
	public final static String RESETPWD_TOKEN_EXISTED = "忘記密碼動作逾時，請重新進行申請。";
	public final static String RESETPWD_MAIL_SENT = "成功發送密碼驗證信息到信箱，請耐心等候。";
	public final static String RESETPWD_MAIL_NOTEXISTED = "錯誤，可能原因為未找到相關帳號、帳號並未設定通信信箱或是信箱輸入錯誤。";
	public final static String RESETPWD_ERROR = "密碼新設錯誤，請重新仔細輸入。";
}
