package com.csprs.cbw.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csprs.cbw.bean.user.MyRole;


public interface MyRoleRepository extends JpaRepository<MyRole, Long> {
	
	List<MyRole> findByName(String name);
	
}
