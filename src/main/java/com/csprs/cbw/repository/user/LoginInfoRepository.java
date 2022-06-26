package com.csprs.cbw.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csprs.cbw.bean.user.LoginInfo;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
	
	List<LoginInfo> findAll();

}
