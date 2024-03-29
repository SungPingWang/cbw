package com.csprs.cbw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.csprs.cbw.filter.ResetPwdFilter;
import com.csprs.cbw.filter.ValidateCodeFilter;
import com.csprs.cbw.filter.VertifyPwdFilter;
import com.csprs.cbw.handler.LoginFailureHandler;
import com.csprs.cbw.handler.LoginSuccessHandler;
import com.csprs.cbw.service.security.UserSecurityService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	@Autowired
    private ValidateCodeFilter validateCodeFilter;
	@Autowired
    private VertifyPwdFilter vertifyPwdFilter;
	@Autowired
	private ResetPwdFilter resetPwdFilter;
	
	@Bean
	UserDetailsService UserService() {
		return new UserSecurityService();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/favicon.ico", "/assets/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.addFilterBefore(vertifyPwdFilter, UsernamePasswordAuthenticationFilter.class) // 添加寄信效驗過濾器
			.addFilterBefore(resetPwdFilter, UsernamePasswordAuthenticationFilter.class) // 添加重寫密碼效驗過濾器
			.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加驗證碼效驗過濾器
			.authorizeRequests()
			.antMatchers("/static", "register").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/error/**").permitAll()
			.antMatchers("/code/image").permitAll()
			.antMatchers("/image/**").permitAll()
			.antMatchers("/handleLogin/forgotPwd").permitAll()
			.antMatchers("/handleLogin/vertify").permitAll()
			.antMatchers("/handleLogin/lock").permitAll()
			.antMatchers("/cwb/**").hasAnyRole("USER", "MANAGER", "ADMIN")
			.antMatchers("/permission/**").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login").defaultSuccessUrl("/cwb/index")
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailureHandler)
			.and()
            .exceptionHandling().accessDeniedPage("/error/403")
			.and()
			.logout()
			.and()
			.csrf().disable();
            //.rememberMe()
            //.tokenValiditySeconds(3600);
	}
	
	
	
}
