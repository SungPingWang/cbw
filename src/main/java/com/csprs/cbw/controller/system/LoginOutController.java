package com.csprs.cbw.controller.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csprs.cbw.util.MailService;
import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;
import com.csprs.cbw.service.security.TimeLimitService;
import com.csprs.cbw.util.Constant;
import com.csprs.cbw.util.EncryptDecrypt;

@Controller
@RequestMapping("/handleLogin")
public class LoginOutController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");

	@Value("${codec.key}")
	private String KEY;
	@Value("${codec.transformation}")
	private String TRANSFORMATION;
	@Value("${codec.algorithm}")
	private String ALGORITHM;
	@Value("${codec.tokenExpired}")
	private String TOKEN_EXPIRED;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyProfileDaoImpl myProfileDaoImpl;
	@Autowired
	MailService mailService;
	@Autowired
	private TimeLimitService restPwdTimeLimitService;


	// 忘記密碼頁面
	@GetMapping("/forgotPwd")
	public String forgotPwdGet() {
		return "system/forgotPassword.html";
	}
	
	// 忘記密碼送出POST
	@PostMapping("/forgotPwd")
	public String forgotPwdPost(String acc, 
			RedirectAttributes redirectAttributes) throws Exception {
		// 1 設定token逾時時間
		Long datetime = System.currentTimeMillis() + Long.parseLong(TOKEN_EXPIRED);
		// 2 判斷帳號是不是在資料庫內的
		// 3 這裡要多判斷對應的帳號是不是有mail
		MyProfile profile = myProfileDaoImpl.findByName(acc);
		if(profile != null && profile.getMail() != null && !"".equals(profile.getMail().trim())) {
			// 4 和輸入的帳號進行組合
			String concatToken = acc + ":" + datetime;
			// 5 加密token
			String token = EncryptDecrypt.encryptAES(concatToken, KEY, TRANSFORMATION, ALGORITHM);
			// 6 傳送mail到指定的帳號信箱內
			mailService.sendingForgotPwdMail(token, profile.getMail());
			logger.info("發送" + acc + "重設密碼驗證信息： " + profile.getMail());
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_MAIL_SENT);
			return "redirect:/login";
		}else { // 如果找不到mail的話就只能回到忘記密碼的頁面了
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_MAIL_NOTEXISTED);
			return "redirect:/handleLogin/forgotPwd";
		}
	}
	
	// GMAIL上面點擊驗證token連接
	@GetMapping("/vertify")
	public String forgotPwdVertify(
			@RequestParam(value = "token")String token, 
			RedirectAttributes redirectAttributes) throws Exception {
		// 1 獲取token並且解密
		String concatToken = EncryptDecrypt.decryptAES(token, KEY, TRANSFORMATION, ALGORITHM);
		// 2 獲取token解密後的帳號以及逾時
		String account = concatToken.split(Constant.COLON)[0];
		long expired = Long.parseLong(concatToken.split(Constant.COLON)[1]);
		// 3 判斷token是否逾時
		if (expired < System.currentTimeMillis()) {
			logger.warn("逾期嘗試進行密碼重設登入： " + account);
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_TOKEN_EXISTED);
			return "redirect:/login";
		} else {
			// 4 獲取account相關的密碼
			MyProfile profile = myProfileDaoImpl.findByName(account);
			// 5 設定帳號密碼的security
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(account, profile.getPassword());
		    Authentication authentication = authenticationManager.authenticate(authRequest);
		    SecurityContext securityContext = SecurityContextHolder.getContext();
		    securityContext.setAuthentication(authentication);
		    logger.info("系統代為登入密碼重設成功： " + account);
			return "system/resetPassword.html";
		}
	}
	
	@PostMapping("/resetPwd")
	public String resetPwd(String pwd1, String pwd2, 
			Map<String, Object> map, 
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		
		SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
		boolean isLimit = restPwdTimeLimitService.CountLimitProccess(
				new ServletWebRequest(request), 
				sessionStrategy, 
        		Constant.SESSION_LOGIN_ERROR_COUNT_NAME, 
        		Constant.SESSION_MAX_RESET_PWD_ERROR_COUNT,
        		Constant.SESSION_RESET_PWD_ERROR_DUETIME_NAME);
		if (isLimit) {
			return "error/lock.html";
		}
       
		
		String account = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!pwd1.equals(pwd2) || pwd1 == null || pwd2 == null) {
			logger.warn("密碼重設失敗： " + account);
			map.put("errorMsg", Constant.RESETPWD_ERROR);
			
			session.setAttribute(Constant.SESSION_RESET_PWD_ERROR_COUNT_NAME, 1);
			
			
			return "system/resetPassword.html";
		}else {
			logger.info("密碼重設成功： " + account);
			session.setAttribute(Constant.VERTIFY_SESSION, "0");
			return "redirect:/cwb/index";
		}
		
	}
	
	@GetMapping("/lock")
	public String lock() {
		return "error/lock.html"; 
	}
	
}
