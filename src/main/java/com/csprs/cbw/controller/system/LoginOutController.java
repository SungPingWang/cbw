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
import com.csprs.cbw.util.Utils;
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


	// ??????????????????
	@GetMapping("/forgotPwd")
	public String forgotPwdGet() {
		return "system/forgotPassword.html";
	}
	
	// ??????????????????POST
	@PostMapping("/forgotPwd")
	public String forgotPwdPost(String acc, String mail,
			RedirectAttributes redirectAttributes) throws Exception {
		// 1 ??????token????????????
		Long datetime = System.currentTimeMillis() + Long.parseLong(TOKEN_EXPIRED);
		// 2 ???????????????????????????????????????
		// 3 ?????????????????????????????????????????????mail
		MyProfile profile = myProfileDaoImpl.findByName(acc);
		if(profile != null && !Utils.isNullEmptyString(profile.getMail()) && profile.getMail().equals(mail)){
			// 4 ??????????????????????????????
			String concatToken = acc + ":" + datetime;
			// 5 ??????token
			String token = EncryptDecrypt.encryptAES(concatToken, KEY, TRANSFORMATION, ALGORITHM);
			// 6 ??????mail???????????????????????????
			mailService.sendingForgotPwdMail(token, profile.getMail());
			logger.info("??????" + acc + "??????????????????????????? " + profile.getMail());
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_MAIL_SENT);
			return "redirect:/login";
		}else { // ???????????????mail?????????????????????????????????????????????
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_MAIL_NOTEXISTED);
			return "redirect:/handleLogin/forgotPwd";
		}
	}
	
	// GMAIL??????????????????token??????
	@GetMapping("/vertify")
	public String forgotPwdVertify(
			@RequestParam(value = "token")String token, 
			RedirectAttributes redirectAttributes) throws Exception {
		// 1 ??????token????????????
		String concatToken = EncryptDecrypt.decryptAES(token, KEY, TRANSFORMATION, ALGORITHM);
		// 2 ??????token??????????????????????????????
		String account = concatToken.split(Constant.COLON)[0];
		long expired = Long.parseLong(concatToken.split(Constant.COLON)[1]);
		// 3 ??????token????????????
		if (expired < System.currentTimeMillis()) {
			logger.warn("??????????????????????????????????????? " + account);
			redirectAttributes.addFlashAttribute("errorMsg", Constant.RESETPWD_TOKEN_EXISTED);
			return "redirect:/login";
		} else {
			// 4 ??????account???????????????
			MyProfile profile = myProfileDaoImpl.findByName(account);
			// 5 ?????????????????????security
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(account, profile.getPassword());
		    Authentication authentication = authenticationManager.authenticate(authRequest);
		    SecurityContext securityContext = SecurityContextHolder.getContext();
		    securityContext.setAuthentication(authentication);
		    logger.info("??????????????????????????????????????? " + account);
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
			logger.warn("????????????????????? " + account);
			map.put("errorMsg", Constant.RESETPWD_ERROR);
			
			session.setAttribute(Constant.SESSION_RESET_PWD_ERROR_COUNT_NAME, 1);
			
			
			return "system/resetPassword.html";
		}else {
			logger.info("????????????????????? " + account);
			session.setAttribute(Constant.VERTIFY_SESSION, "0");
			return "redirect:/cwb/index";
		}
		
	}
	
	@GetMapping("/lock")
	public String lock() {
		return "error/lock.html"; 
	}
	
}
