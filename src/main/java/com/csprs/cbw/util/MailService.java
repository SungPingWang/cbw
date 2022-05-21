package com.csprs.cbw.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	JavaMailSender mailSender;
	
	// 無方法返回值調用
	@Async("executor")
	public void sendingForgotPwdMail(String token, String mail) {
		
		Calendar calendar2 = Calendar.getInstance();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		calendar2.add(Calendar.DATE, Constant.RESET_PWD_MAILSENDER_EXPIRED);
		String three_days_after = sdf2.format(calendar2.getTime());
		System.out.println(three_days_after);
		
		try {
			StringBuffer bf = new StringBuffer();
			bf.append("你已進行密碼重設動作，若無問題請於").append(three_days_after).append("內點擊");
			bf.append("<a href='http://localhost:8080/csprscbw/handleLogin/vertify?token="+ token +"'>此連結按鈕</a>");
			bf.append("進行密碼重設，逾期將無效。感謝您。");
 			// SimpleMailMessage message = new SimpleMailMessage();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(mail);
			helper.setSubject("您已申請忘記您的密碼。請您於一日內點選以下連結進行密碼重設操作, 謝謝!");
			message.setContent(bf.toString(), "text/html;charset=utf-8");
 			
 			mailSender.send(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	// 有方法返回值
	@Async("executor")
	public Future<String> testAsyncString() throws InterruptedException {
		Future<String> future;
		Thread.sleep(5000);
		logger.info("doing the Async String");
		future = new AsyncResult<String>("return the Async String");
		return future;
	}
	
}
