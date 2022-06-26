package com.csprs.cbw.service.user;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.LoginInfo;
import com.csprs.cbw.repository.user.LoginInfoRepository;

@Service
public class LoginInfoService {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private LoginInfoRepository loginInfoRepository;
	
	public List<LoginInfo> findAll(){
		List<LoginInfo> findAll = loginInfoRepository.findAll();
		return findAll;
	}
	
	public void save(LoginInfo loginInfo) {
		loginInfoRepository.save(loginInfo);
	}
	
}
