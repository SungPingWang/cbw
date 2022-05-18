package com.csprs.cbw.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.controller.user.AccountController;
import com.csprs.cbw.repository.user.MyProfileRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("all")
public class UserSecurityService implements UserDetailsService {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	@Autowired
	private MyProfileRepository myProfileRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// MyProfile account = myProfileRepository.findByName("Lui").get(0);
		MyProfile account = null;
		String[] roleList = null;
		
		try {
			account = myProfileRepository.findByName(username).get(0);
			roleList = account.getRoles().split(",");
			Integer role_size = roleList.length;
		} catch (Exception e) {
			
		}
		
		
		//if("admin".equals(username)) {
		
		if(account != null) {
			UserDetails userDetails = User.builder()
	                //.username("admin")
					.username(account.getName())
	                //.password("{noop}" + Password.getCorrectPassword()) // 設定這裡必須式的密碼
	                .password("{noop}" + account.getPassword())
	                //.roles("ADMIN", "USER")
	                .roles(roleList)
	                .build();
	        
	        return userDetails;
		}
		throw new UsernameNotFoundException("user not found");
	}
	

}
