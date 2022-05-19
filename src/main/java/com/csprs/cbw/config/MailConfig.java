package com.csprs.cbw.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${mail.username}")
	private String MAIL_USER;
	@Value("${mail.host}")
	private String MAIL_HOST;
	@Value("#{new Integer('${mail.port}')}")
	private Integer MAIL_PORT;
	@Value("${mail.password}")
	private String MAIL_PWD;
	
	@Value("${mail.transport.protocol}")
	private String PROTOCOL;
	
	@Value("#{new Boolean('${mail.smtp.auth}')}")
	private Boolean SMTP_AUTH;
	
	@Value("#{new Boolean('${mail.smtp.starttls.enable}')}")
	private Boolean SMTP_STARTTLS_ENABLED;
	
	@Value("#{new Boolean('${mail.smtp.starttls.required}')}")
	private Boolean SMTP_STARTTLS_REQUIRED;
	
	@Value("#{new Boolean('${mail.debug}')}")
	private Boolean MAIL_DEBUG;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(MAIL_HOST);
		mailSender.setPort(MAIL_PORT);

		mailSender.setUsername(MAIL_USER);
		mailSender.setPassword(MAIL_PWD);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", PROTOCOL);
		props.put("mail.smtp.auth", SMTP_AUTH);
		props.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLED);
		props.put("mail.smtp.starttls.required", SMTP_STARTTLS_REQUIRED);
		props.put("mail.debug", MAIL_DEBUG);
		return mailSender;
	}
	
}
