package com.csprs.cbw.dao.user;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.repository.user.MyRoleRepository;


@Service
public class MyRoleDaoImpl {
	@Autowired
	MyRoleRepository myRoleRepository;
	
	@Transactional
	public void saveRole(MyRole role) {
		myRoleRepository.save(role);
	}
	
	@Transactional
	public MyRole getRoleByName(String roleName) {
		MyRole role = new MyRole();
		try {
			role = myRoleRepository.findByName(roleName).get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return role;
	}
}
